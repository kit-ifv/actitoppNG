package edu.kit.ifv.mobitopp.actitopp

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

    override val id: Int = idCounter

    override val gender: Gender = Gender.fromCode(genderCode)
    override val employment: Employment = Employment.fromInt(employmentCode)
    override val isAllowedToWork: Boolean = true
//    var weekPattern = HWeekPattern(this)


    init {
        household.addHouseholdmember(this, persNrinHousehold)
    }






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


    }
}