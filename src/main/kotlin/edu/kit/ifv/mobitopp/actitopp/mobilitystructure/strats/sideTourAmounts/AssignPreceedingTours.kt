package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.HTour
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiableDayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.modernization.MutableTourStructure
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import kotlin.math.sign

data class PrecedingInput(
    val personInfo: PersonWithRoutine,
    val day: DayStructure,
    val currentPlannedTourAmounts: PlannedTourAmounts,
    val previousPlannedTourAmounts: PlannedTourAmounts?,

    )


fun interface GenerateSideTours {
    fun generate(precedingInput: PrecedingInput): Int

}
