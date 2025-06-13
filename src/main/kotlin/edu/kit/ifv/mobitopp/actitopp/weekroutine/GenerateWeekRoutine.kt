package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper

/**
 * The first step of generation requires the generation of a week routine to establish the rest of the mobility pattenr
 *
 */
fun interface GenerateWeekRoutine {
    fun generate(person: IPerson, rng: RNGHelper): WeekRoutine
}

fun IPerson.generateWeekRoutine(
    rng: RNGHelper,
    strategy: GenerateWeekRoutine = DefaultWeekRoutineGeneration(),
): WeekRoutine {
    return strategy.generate(this, rng)
}