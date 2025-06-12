package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.IO.DebugLoggers
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender
import edu.kit.ifv.mobitopp.actitopp.enums.JointStatus
import edu.kit.ifv.mobitopp.actitopp.enums.isEmployedAnywhere
import edu.kit.ifv.mobitopp.actitopp.enums.isStudentOrAzubi
import org.jetbrains.annotations.TestOnly
import java.util.Collections
import kotlin.math.max

// TODO once refactored, rename or remove

interface IPerson {
    val children0_10: Int
    val children_u18: Int
    val employment: Employment
    val age: Int
    val areatype: AreaType
    val gender: Gender
    val maxCommute: Double
    val numberofcarsinhousehold: Int
    val isAllowedToWork: Boolean
    val commutingdistance_work: Double
    val commutingdistance_education : Double

    val id: Int

    fun isAnywayEmployed(): Boolean
    fun isinEducation(): Boolean
}

fun IPerson.spawnRandomGenerator(offset: Long = 0L): RNGHelper {
    return RNGHelper(id.toLong() + offset)
}

class ActitoppPerson@JvmOverloads constructor(
    val household: ActiToppHousehold,
    val persNrinHousehold: Int,
    val persIndex: Int,
    override val age: Int,
    employmentCode: Int,
    genderCode: Int,
    override val commutingdistance_work: Double = 0.0,
    override val commutingdistance_education: Double = .0,
) :IPerson {
    override val maxCommute: Double =
        max(commutingdistance_work, commutingdistance_education)

    override val id: Int = ActitoppPerson.idCounter
    private val attributes: MutableMap<String, Double> = mutableMapOf()

    @TestOnly
    internal fun getMutableMapForTest(): MutableMap<String, Double> = attributes
    override val gender: Gender = Gender.fromCode(genderCode)
    override val employment: Employment = Employment.fromInt(employmentCode)
    override val isAllowedToWork: Boolean = true
    var weekPattern: HWeekPattern = HWeekPattern(this)


    init {
        household.addHouseholdmember(this, persNrinHousehold)
        attributes["numbermodeledinhh"] = persNrinHousehold.toDouble()
    }

    var activityCounter = 0
        get() {
            return field.also { field++ }
        }
        private set

    var tourCounter = 0
        get() {
            return field.also { field++ }
        }
        private set
    constructor(tmppers: ActitoppPerson, tmphh: ActiToppHousehold) : this(
        tmphh,
        tmppers.persNrinHousehold,
        tmppers.persIndex,
        tmppers.age,
        tmppers.employment.code,
        tmppers.gender.code,
        tmppers.commutingdistance_work,
        tmppers.commutingdistance_education
    )


    // stores all attributes that are not directly accessible by variables


    /**
     * @return the weekPattern
     */
    // stores all activity information of the week pattern

    fun days() = weekPattern.days

    /**
     * Robin: This function delegates the call to the weekpattern, which should not be exposed
     */
    fun countActivityTypes(activityType: ActivityType): Int {
        return weekPattern.countActivitiesPerWeek(activityType)
    }

    fun countTourTypes(activityType: ActivityType): Int = weekPattern.countToursPerWeek(activityType)
    fun countDaysWithSpecificActivity(activityType: ActivityType): Int =
        weekPattern.countDaysWithSpecificActivity(activityType)



    override val children0_10: Int
        /**
         * @return the children0_10
         */
        get() = household.children0_10

    override val children_u18: Int
        /**
         * @return the children_u18
         */
        get() = household.children_u18


    override val areatype: AreaType
        /**
         * @return the areatype
         */
        get() = household.areatype


    override val numberofcarsinhousehold: Int
        /**
         * @return the numberofcarsinhousehold
         */
        get() = household.numberofcarsinhousehold


    val commutingDuration_work: Int
        /**
         * @return the commutingduration_work [min]
         */
        get() {
            /*
              * mean commuting speed in kilometers/hour is calculated using commuting distance groups
              * based on data of commuting trips of the German Mobility Panel (2004-2013)
              */
            val commutingspeed_work = if (commutingdistance_work > 0 && commutingdistance_work <= 5) 16.0
            else if (commutingdistance_work > 5 && commutingdistance_work <= 10) 29.0
            else if (commutingdistance_work > 10 && commutingdistance_work <= 20) 38.0
            else if (commutingdistance_work > 20 && commutingdistance_work <= 50) 51.0
            else if (commutingdistance_work > 50) 67.0
            else 32.0

            // minimum trip duration: 1 Minute
            return max(
                1.0,
                Math.round((commutingdistance_work / commutingspeed_work) * 60).toDouble()
            ).toInt()
        }


    val commutingDuration_education: Int
        /**
         * @return the commutingduration_education [min]
         */
        get() {
            /*
              * mean commuting speed in kilometers/hour is calculated using commuting distance groups
              * based on data of commuting trips of the German Mobility Panel (2004-2013)
              */
            val commutingspeed_education = if (commutingdistance_education > 0 && commutingdistance_education <= 5) 12.0
            else if (commutingdistance_education > 5 && commutingdistance_education <= 10) 21.0
            else if (commutingdistance_education > 10 && commutingdistance_education <= 20) 28.0
            else if (commutingdistance_education > 20 && commutingdistance_education <= 50) 40.0
            else if (commutingdistance_education > 50) 55.0
            else 21.0

            // minimum trip duration: 1 Minute
            return max(
                1.0,
                Math.round((commutingdistance_education / commutingspeed_education) * 60).toDouble()
            ).toInt()
        }


    /**
     * @param name
     * @param value
     */
    @Deprecated("Use set operator")
    fun addAttributetoMap(name: String, value: Double) {
        attributes[name] = value
    }


    fun addBudget(activityType: ActivityType, name: String, value: Double) =
        addAttributetoMap("${activityType.typeasChar}$name", value)

    /**
     * @param name
     * @return
     */
    operator fun get(name: String) = attributesMap[name]
    operator fun set(name: String, value: Double) {
        attributes[name] = value
    }

    operator fun get(activityType: ActivityType, name: String): Double =
        attributesMap["${activityType.typeasChar}$name"]

    operator fun set(activityType: ActivityType, name: String, value: Double) = addBudget(activityType, name, value)

    /**
     * Replaces old naked string access of "budget_category_alternative"
     */
    fun categoryAlternative(activityType: ActivityType) = get(activityType, "budget_category_alternative")
    fun categoryAlternative()  = ActivityType.OUTOFHOMEACTIVITY.associateWith { categoryAlternative(it) }
    fun categoryIndex(activityType: ActivityType) = get(activityType, "budget_category_index")
    fun budgetExact(activityType: ActivityType) = get(activityType, "budget_exact")

    @Deprecated("Never ever allow free access via strings, thats just bad design")
    fun getAttributefromMap(name: String): Double {
        return attributes[name]!!
    }



    val attributesMap: DefaultDoubleMap<String>
        /**
         * @return the attributes
         */
        get() = DefaultDoubleMap(attributes)




    override fun toString(): String {
        val message = StringBuffer()

        message.append("\n person information")

        message.append("\n - persindex : ")
        message.append(persIndex)

        message.append("\n - age : ")
        message.append(age)

        message.append("\n - employment type : ")
        message.append(employment)

        message.append("\n - gender : ")
        message.append(gender)

        message.append("\n - is allowed to work : ")
        message.append(isAllowedToWork)

        message.append("\n - commuting distance work : ")
        message.append(commutingdistance_work)

        message.append("\n - commuting distance education : ")
        message.append(commutingdistance_education)

        message.append("\n - household ")
        message.append(household)

        return message.toString()
    }





//    /**
//     * @param aktliste
//     */
//    fun setAllJointActivitiesforConsideration(aktliste: MutableList<HActivity>) {
//        this.jointActivitiesforConsideration = aktliste
//    }

    /**
     * determines if a person is anyway employed (full time, part time or in vocational program)
     *
     * @return
     */
    override fun isAnywayEmployed(): Boolean {
        return employment.isEmployedAnywhere()
    }

    /**
     * determines if a person is in school or student
     *
     * @return
     */
    override fun isinEducation(): Boolean {
        return employment.isStudentOrAzubi()
    }





    companion object {


        private var idCounter = 0
            get() = field.also { field++ }

        @JvmOverloads
        operator fun invoke(
            PersIndex: Int,
            children0_10: Int,
            children_u18: Int,
            age: Int,
            employment: Int,
            gender: Int,
            areatype: Int,
            numberofcarsinhousehold: Int = 0,
            commutingdistance_work: Double = .0,
            commutingdistance_education: Double = .0,
        ): ActitoppPerson {
            val household = ActiToppHousehold(
                children0_10,
                children_u18,
                areatype,
                numberofcarsinhousehold
            )

            val person = ActitoppPerson(
                household,
                1,
                PersIndex,
                age,
                employment,
                gender,
                commutingdistance_work,
                commutingdistance_education
            )

            return person

        }
    }
}

/**
 * Because very often if something doesnt exist legacy actitopp returned 0.0
 */
class DefaultDoubleMap<K>(val original: Map<K, Double>) : Map<K, Double> by original {
    override operator fun get(key: K): Double {
        return original.getOrDefault(key, 0.0)
    }

    override fun toString(): String {
        return original.toString()
    }
}