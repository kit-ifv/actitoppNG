package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts


import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts

data class PrecedingInput(
    val personInfo: PersonWithRoutine,
    val day: DayStructure,
    val currentPlannedTourAmounts: PlannedTourAmounts,
    val previousPlannedTourAmounts: PlannedTourAmounts?,

    )


fun interface GenerateSideTours {
    fun generate(precedingInput: PrecedingInput): Int

}
