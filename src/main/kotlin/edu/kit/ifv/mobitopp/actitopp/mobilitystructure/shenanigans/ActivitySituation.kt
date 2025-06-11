package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HActivity
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.HTour
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.utils.Position
import edu.kit.ifv.mobitopp.actitopp.modernization.MutableTourStructure
import edu.kit.ifv.mobitopp.actitopp.steps.ActivityAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.ActivityAttributesByElement
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributesFromElement
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributesFromStructure
import edu.kit.ifv.mobitopp.actitopp.steps.FullyQualifiedDayStructureAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.RoutineAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.TourAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.TourAttributesByElement
import edu.kit.ifv.mobitopp.actitopp.steps.TourAttributesByStructAndNumbers
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ChoiceSituation


class ActivitySituation private constructor(
    override val choice: ActivityType,
    personAndRoutineAttributes: PersonAndRoutineAttributes,
    dayAttributes: FullyQualifiedDayStructureAttributes,
    tourAttributes: TourAttributes,
    activityAttributes: ActivityAttributes,
) :
    ChoiceSituation<ActivityType>(), TourAttributes by tourAttributes, PersonAttributes by personAndRoutineAttributes,
    RoutineAttributes by personAndRoutineAttributes, FullyQualifiedDayStructureAttributes by dayAttributes,
    ActivityAttributes by activityAttributes {

    constructor(
        choice: ActivityType,
        person: ActitoppPerson,
        routine: WeekRoutine,
        day: HDay,
        tour: HTour,
        activity: HActivity,
    ) : this(
        choice,
        PersonAndRoutineFrom(PersonWithRoutine(person, routine)),
        DayAttributesFromElement(day),
        TourAttributesByElement(tour),
        ActivityAttributesByElement(activity)
    )

    constructor(
        choice: ActivityType,
        personWithRoutine: PersonWithRoutine,
        dayStructure: DayStructure,
        tourStructure: BidirectionalIndexedValue<MutableTourStructure>,
        position: Position,
        plannedTourAmounts: PlannedTourAmounts,
    ) : this(
        choice,
        PersonAndRoutineFrom(personWithRoutine),
        DayAttributesFromStructure(dayStructure),
        TourAttributesByStructAndNumbers(tourStructure, plannedTourAmounts.precursorAmount, plannedTourAmounts.successorAmount),
        ActivityAttributes { position == Position.BEFORE }
    )

}


