package edu.kit.ifv.mobitopp.actitoppNG.weekroutine

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.Person
import kotlin.random.Random

/**
 * The first step of the activity generation is to create an appropriate [WeekRoutine] for the input person. The
 * week routine will contain information about how many days of a specific activity are performed by a person, as
 * well as the amount of tours and activities. This interface is tasked with creating such a [WeekRoutine] and
 * subsequent implementations are tasked with determining the proper [WeekRoutine] for an input [person][Person]
 */
fun interface GenerateWeekRoutine {
    context(rng: Random)
    fun generate(person: Person): WeekRoutine
}

/**
 * A convenience extension function to call the generation of a [WeekRoutine] directly on an [Person] object.
 */
context(rng: Random, planGenerationParameters: PlanGenerationParameters, models: AllChoiceModels)
fun Person.generateWeekRoutine(
    strategy: GenerateWeekRoutine = DefaultWeekRoutineGeneration(planGenerationParameters, models),
): WeekRoutine {
    return strategy.generate(this)
}