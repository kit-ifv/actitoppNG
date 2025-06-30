package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans


import discreteChoice.models.ChoiceAlternative
import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitoppNG.steps.ActivityAmountAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.ActivityAmountByNumber
import edu.kit.ifv.mobitopp.actitoppNG.steps.DayAttributesFromStructure
import edu.kit.ifv.mobitopp.actitoppNG.steps.FullyQualifiedDayStructureAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.RoutineAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.TourAttributes
import edu.kit.ifv.mobitopp.actitoppNG.steps.TourAttributesByIndexedStructure
import edu.kit.ifv.mobitopp.actitoppNG.steps.TourPositionAttributes
import edu.kit.ifv.mobitopp.actitoppNG.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.WeekRoutine


class TourAlternative private constructor(
    override val choice: ActivityType, personAndRoutineAttributes: PersonAndRoutineAttributes,
    dayAttributes: FullyQualifiedDayStructureAttributes, tourAttributes: TourPositionAttributes,
) :
    ChoiceAlternative<ActivityType>(), TourPositionAttributes by tourAttributes,
    PersonAttributes by personAndRoutineAttributes,
    RoutineAttributes by personAndRoutineAttributes, FullyQualifiedDayStructureAttributes by dayAttributes {


    constructor(
        choice: ActivityType,
        person: Person,
        routine: WeekRoutine,
        day: DayStructure,
        tourAttributes: TourPositionAttributes,
    ) : this(
        choice,
        PersonAndRoutineFrom(PersonWithRoutine(person, routine)),
        DayAttributesFromStructure(day),
        tourAttributes
    )

}

class TourAlternativeInt private constructor(
    override val choice: Int, personAndRoutineAttributes: PersonAndRoutineAttributes,
    dayAttributes: FullyQualifiedDayStructureAttributes, tourAttributes: TourAttributes,
    activityAmountAttributes: ActivityAmountAttributes,

    ) :
    ChoiceAlternative<Int>(), TourAttributes by tourAttributes, PersonAttributes by personAndRoutineAttributes,
    RoutineAttributes by personAndRoutineAttributes, FullyQualifiedDayStructureAttributes by dayAttributes,
    ActivityAmountAttributes by activityAmountAttributes {

    constructor(
        choice: Int,
        person: Person,
        routine: WeekRoutine,
        day: DayStructure,
        tour: BidirectionalIndexedValue<TourStructure>,
        amountOfPrecursorActivities: Int,
    ) : this(
        choice,
        PersonAndRoutineFrom(PersonWithRoutine(person, routine)),
        DayAttributesFromStructure(day),
        TourAttributesByIndexedStructure(tour),
        ActivityAmountByNumber(amountOfPrecursorActivities),
    )


}

