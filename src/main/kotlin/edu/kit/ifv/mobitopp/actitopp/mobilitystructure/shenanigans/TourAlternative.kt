package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans


import discreteChoice.models.ChoiceAlternative
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitopp.steps.ActivityAmountAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.ActivityAmountByNumber
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributesFromStructure
import edu.kit.ifv.mobitopp.actitopp.steps.FullyQualifiedDayStructureAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.RoutineAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.TourAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.TourAttributesByIndexedStructure
import edu.kit.ifv.mobitopp.actitopp.steps.TourPositionAttributes
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine


class TourAlternative private constructor(
    override val choice: ActivityType, personAndRoutineAttributes: PersonAndRoutineAttributes,
    dayAttributes: FullyQualifiedDayStructureAttributes, tourAttributes: TourPositionAttributes,
) :
    ChoiceAlternative<ActivityType>(), TourPositionAttributes by tourAttributes,
    PersonAttributes by personAndRoutineAttributes,
    RoutineAttributes by personAndRoutineAttributes, FullyQualifiedDayStructureAttributes by dayAttributes {


    constructor(
        choice: ActivityType,
        person: IPerson,
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
        person: IPerson,
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

