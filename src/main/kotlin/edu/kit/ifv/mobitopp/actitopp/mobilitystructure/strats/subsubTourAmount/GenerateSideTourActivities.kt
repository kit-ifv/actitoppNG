package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.subsubTourAmount

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

fun interface GenerateSideTourActivities {

    fun generateActivityAmount(input: SideTourActivityInput): Int
}

data class SideTourActivityInput(
    val person: IPerson,
    val routine: WeekRoutine,
    val currentDay: DayStructure,
    val tour: BidirectionalIndexedValue<TourStructure>,
    val minimumAmountOfActivities: Int,
    val amountOfActivitiesBeforeMainAct: Int,
)