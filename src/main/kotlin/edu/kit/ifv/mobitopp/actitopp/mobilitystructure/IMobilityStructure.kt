package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.DetermineTripDuration
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.timebudgets.TimeBudgets
import java.util.NavigableSet

interface IMobilityStructure {
    val days: NavigableSet<DayStructure>

    fun toPlan(tripDuration: DetermineTripDuration, timeBudgets: TimeBudgets): MobilityPlan
}