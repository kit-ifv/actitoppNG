package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper

/**
 * The first step of the activity generation is to create an appropriate [WeekRoutine] for the input person. The
 * week routine will contain information about how many days of a specific activity are performed by a person, as
 * well as the amount of tours and activities. This interface is tasked with creating such a [WeekRoutine] and
 * subsequent implementations are tasked with determining the proper [WeekRoutine] for an input [person][IPerson]
 */
fun interface GenerateWeekRoutine {
    fun generate(person: IPerson, rng: RNGHelper): WeekRoutine
}

/**
 * A convenience extension function to call the generation of a [WeekRoutine] directly on an [IPerson] object.
 */
fun IPerson.generateWeekRoutine(
    rng: RNGHelper,
    strategy: GenerateWeekRoutine = DefaultWeekRoutineGeneration(),
): WeekRoutine {
    return strategy.generate(this, rng)
}