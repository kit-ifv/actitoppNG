package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender
import edu.kit.ifv.mobitopp.actitopp.enums.isEmployedAnywhere
import edu.kit.ifv.mobitopp.actitopp.enums.isStudentOrAzubi
import kotlin.math.max



data class PersonAttributes(
    val gender: Gender,
    val employment: Employment,
    val age: Int,
    val commuteDistanceWork: Double? = null,
    val commuteDistanceEducation: Double? = null,
    val isAllowedToWork: Boolean = true
)

class ActitoppPerson@JvmOverloads constructor(
    val household: ActiToppHousehold,
    val persNrinHousehold: Int,
    override val age: Int,
    employmentCode: Int,
    genderCode: Int,
    override val commutingdistance_work: Double = 0.0,
    override val commutingdistance_education: Double = .0,
) :IPerson {

    init {
        household.addHouseholdmember(this, persNrinHousehold)
    }

    override val maxCommute: Double =
        max(commutingdistance_work, commutingdistance_education)

    override val id: Int = idCounter

    override val gender: Gender = Gender.fromCode(genderCode)
    override val employment: Employment = Employment.fromInt(employmentCode)
    override val isAllowedToWork: Boolean = true



    override val children0_10: Int = household.children0_10

    override val children_u18: Int = household.children_u18


    override val areatype: AreaType = household.areatype


    override val numberofcarsinhousehold: Int = household.numberofcarsinhousehold

    override fun isAnywayEmployed(): Boolean = employment.isEmployedAnywhere()

    override fun isinEducation(): Boolean  = employment.isStudentOrAzubi()






    companion object {


        private var idCounter = 0
            get() = field.also { field++ }


    }
}