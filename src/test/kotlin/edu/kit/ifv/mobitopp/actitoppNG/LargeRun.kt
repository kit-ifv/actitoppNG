package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModernizedActivity
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ReusablePlanGeneration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.time.measureTime




suspend fun List<ActitoppPerson>.generateSchedules(): List<List<ModernizedActivity>> = coroutineScope {

    val params = PlanGenerationParameters()
    val models = AllChoiceModels.create(params)
    val householdPlan = DefaultPlanGeneration(models)
    this@generateSchedules.withIndex().map { (index, person) ->
        async(Default) {
            context(params, models) {
                val schedule = householdPlan.generate(person).finish()
                if (index % 100 == 0) println("Working on person $index done")
                schedule
            }
        }
    }.awaitAll()
}

fun main() {
    val targets = randomHouseholds(500000).flatMap { it.generatePersons(5) }
    val duration = measureTime {

        val lastActivity = runBlocking {
            targets.generateSchedules()
        }.last()
        println(lastActivity)
    }
    println("Took ${duration}")
}