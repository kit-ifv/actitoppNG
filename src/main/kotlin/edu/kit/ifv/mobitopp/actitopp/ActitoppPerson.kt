package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.Gender
import edu.kit.ifv.mobitopp.actitopp.enums.isEmployedAnywhere
import edu.kit.ifv.mobitopp.actitopp.enums.isStudentOrAzubi
import kotlin.math.max

class ActitoppPerson(
    override val household: ActiToppHousehold,
    val attributes: PersonAttributes,
) : Person {

    init {
        household.add(this)
    }

    override val age: Int = attributes.age
    override val commutingdistanceWork: Double = attributes.commuteDistanceWork ?: .0
    override val commutingdistanceEducation: Double = attributes.commuteDistanceEducation ?: .0

    override val maxCommute: Double =
        max(commutingdistanceWork, commutingdistanceEducation)

    override val id: Int = idCounter

    override val gender: Gender = attributes.gender
    override val employment: Employment = attributes.employment
    override val isAllowedToWork: Boolean = true

    override fun isAnywayEmployed(): Boolean = employment.isEmployedAnywhere()

    override fun isinEducation(): Boolean = employment.isStudentOrAzubi()


    companion object {


        private var idCounter = 0
            get() = field.also { field++ }


    }
}