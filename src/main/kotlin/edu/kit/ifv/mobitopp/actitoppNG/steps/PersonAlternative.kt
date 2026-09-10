package edu.kit.ifv.mobitopp.actitoppNG.steps

import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.Gender
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
import edu.kit.ifv.mobitopp.actitoppNG.performance.KilometerEncoder
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.WeekRoutine

/**
 * This interface is a rewrite of the actitoppPersonParameters Enum, and exists only to be able to cross reference the
 * code. Once the rewrite is complete most of these conditions could be inlined in the utility functions.
 */
interface PersonAttributes {
    val ageCode: Int
    fun isMale(): Boolean
    val commuteDistanceCode: Int
    val employment: Employment
}

class PersonAttributesFromElement(val person: Person) : PersonAttributes {
    override val ageCode: Int = AgeEncoder.encode(person.age)
    override fun isMale() = person.gender == Gender.MALE
    override val commuteDistanceCode: Int = KilometerEncoder.convert(person.maxCommute)
    override val employment = person.employment
}


class PersonAlternative private constructor(

    private val weekRoutine: WeekRoutine,
    val person: Person,
    attributes: PersonAttributesFromElement,
    householdAttributes: HouseholdAttributes,
) : PersonAttributes by attributes, HouseholdAttributes by householdAttributes {


    constructor(weekRoutine: WeekRoutine, person: Person) : this(
        weekRoutine,
        person,
        PersonAttributesFromElement(person),
        HouseholdAttributesFromElement(person.household)
    )


    fun amountOfWorkingDays() = weekRoutine.amountOfWorkingDays
    fun amountOfLeisureDays() = weekRoutine.amountOfLeisureDays
    fun amountOfEducationDays() = weekRoutine.amountOfEducationDays
    fun amountOfShoppingDays() = weekRoutine.amountOfShoppingDays

}

