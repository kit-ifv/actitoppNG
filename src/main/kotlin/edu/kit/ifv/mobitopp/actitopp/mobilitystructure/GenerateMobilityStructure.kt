package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

fun interface GenerateMobilityStructure {
    fun generate(person: IPerson, weekRoutine: WeekRoutine): IMobilityStructure
}