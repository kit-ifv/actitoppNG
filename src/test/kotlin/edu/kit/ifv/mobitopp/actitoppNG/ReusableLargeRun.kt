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
suspend fun List<ActitoppPerson>.generateSchedulesReuse(): List<List<ModernizedActivity>> = coroutineScope {
    val params = PlanGenerationParameters()
    val models = AllChoiceModels.create(params)
    val listSize: Int = this@generateSchedulesReuse.size
    val results  = arrayOfNulls<List<ModernizedActivity>>(listSize)
    val workerCount = Runtime.getRuntime().availableProcessors()

    (0 until workerCount).map { workerId ->

        async(Default) {
            var localI = workerId //  start at the workerId.
            val householdPlanThreadLocal = ReusablePlanGeneration(models)

            while(localI < listSize) {
                val person = this@generateSchedulesReuse[localI]
                context(params, models) {

                    results[localI] = householdPlanThreadLocal.generate(person).finish()
                }
                localI += workerCount // increment the index by the amount of workers.
            }
        }
    }.awaitAll()
    return@coroutineScope results.map { requireNotNull(it) }
}

fun main() {
    val targets = randomHouseholds(500000).flatMap { it.generatePersons(5) }
    val duration = measureTime {

        val lastActivity = runBlocking {
            targets.generateSchedulesReuse()
        }.last()
        println(lastActivity)
    }
    println("Took ${duration}")
}