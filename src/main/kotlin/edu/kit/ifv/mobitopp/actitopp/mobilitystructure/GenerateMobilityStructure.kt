package edu.kit.ifv.mobitopp.actitopp.mobilitystructure

import edu.kit.ifv.mobitopp.actitopp.Person
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

fun interface GenerateMobilityStructure {
    fun generate(person: Person, weekRoutine: WeekRoutine): IMobilityStructure
}