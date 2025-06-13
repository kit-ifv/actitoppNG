package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender
import edu.kit.ifv.mobitopp.actitopp.enums.isEmployedAnywhere
import edu.kit.ifv.mobitopp.actitopp.enums.isStudentOrAzubi
import kotlin.math.max
import kotlin.random.Random


data class PersonAttributes(
    val gender: Gender,
    val employment: Employment,
    val age: Int,
    val commuteDistanceWork: Double? = null,
    val commuteDistanceEducation: Double? = null,
    val isAllowedToWork: Boolean = true,
) {
    companion object {
        fun random(rng: Random): PersonAttributes {
            return PersonAttributes(
                age = rng.nextInt(0, 100),
                employment = Employment.fromInt(rng.nextInt(0, 42)),
                gender = Gender.fromCode(rng.nextInt(0, 3)),
                commuteDistanceWork = rng.nextDouble(),
                commuteDistanceEducation = rng.nextDouble()
            )
        }
    }
}

class ActitoppPerson(
    val household: ActiToppHousehold,
    val attributes: PersonAttributes,
) : IPerson {

    init {
        household.add(this)
    }

    override val age: Int = attributes.age
    override val commutingdistance_work: Double = attributes.commuteDistanceWork ?: .0
    override val commutingdistance_education: Double = attributes.commuteDistanceEducation ?:.0

    override val maxCommute: Double =
        max(commutingdistance_work, commutingdistance_education)

    override val id: Int = idCounter

    override val gender: Gender = attributes.gender
    override val employment: Employment = attributes.employment
    override val isAllowedToWork: Boolean = true


    override val children0_10: Int = household.children0_10

    override val children_u18: Int = household.children_u18


    override val areatype: AreaType = household.areaType


    override val numberofcarsinhousehold: Int = household.numberOfCars

    override fun isAnywayEmployed(): Boolean = employment.isEmployedAnywhere()

    override fun isinEducation(): Boolean = employment.isStudentOrAzubi()


    companion object {


        private var idCounter = 0
            get() = field.also { field++ }


    }
}