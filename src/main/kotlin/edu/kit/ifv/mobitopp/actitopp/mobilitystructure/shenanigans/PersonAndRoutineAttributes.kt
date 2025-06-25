package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans


import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributesFromWeekday
import edu.kit.ifv.mobitopp.actitopp.steps.HouseholdAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.HouseholdAttributesFromElement
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributesFromElement
import edu.kit.ifv.mobitopp.actitopp.steps.RoutineAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.RoutineAttributesFromElement
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ChoiceAlternative
import java.time.DayOfWeek


interface PersonAndRoutineAttributes : PersonAttributes, RoutineAttributes
data class PersonAndRoutineFrom(
    val element: PersonWithRoutine,
    private val routine: RoutineAttributes = RoutineAttributesFromElement(element.routine),
    private val person: PersonAttributes = PersonAttributesFromElement(element.person),
) : PersonAndRoutineAttributes, RoutineAttributes by routine, PersonAttributes by person

class DayAlternative private constructor(
    override val choice: ActivityType,

    private val personAttributesFromElement: PersonAndRoutineAttributes,
    private val dayAttributesFromElement: DayAttributes,
    private val householdAttributesFromElement: HouseholdAttributes,
) :
    ChoiceAlternative<ActivityType>(), PersonAttributes by personAttributesFromElement,
    RoutineAttributes by personAttributesFromElement,
    DayAttributes by dayAttributesFromElement,
HouseholdAttributes by householdAttributesFromElement{
    constructor(choice: ActivityType, personRoutine: PersonWithRoutine, week: DayOfWeek) : this(
        choice, PersonAndRoutineFrom(personRoutine), DayAttributesFromWeekday(week), HouseholdAttributesFromElement(personRoutine.household)
    )

}


