package edu.kit.ifv.mobitopp

import edu.kit.ifv.mobitopp.actitopp.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.ModernizedActivity
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


private val random = Random(1)
private fun generateHousehold(): ActiToppHousehold {
    return ActiToppHousehold(
        random.nextInt(0, 10),
        random.nextInt(0, 10),
        AreaType.fromCode(random.nextInt(0, 10)),
        random.nextInt(0, 10)
    )
}

fun generateHouseholds(amount: Int): List<ActiToppHousehold> {
    return (0..<amount).map { generateHousehold() }
}

fun ActiToppHousehold.generatePersons(amount: Int): List<ActitoppPerson> {
    return (0..<amount).map {
        this.generatePerson()
    }
}

fun ActiToppHousehold.generatePerson(): ActitoppPerson {
    return ActitoppPerson(
        household = this,
        attributes = PersonAttributes.random(random),
    )
}

suspend fun Collection<ActitoppPerson>.generateSchedules(): List<List<ModernizedActivity>> = coroutineScope {


    this@generateSchedules.withIndex().map { (index, person) ->
        async(Default) {
            val householdPlan = DefaultPlanGeneration()
            val schedule = householdPlan.generate(person).finish()
            if (index % 100 == 0) println("Working on person $index done")
            schedule
        }
    }.awaitAll()
//    return withIndex().map { (index, person) ->
//        val householdPlan = DefaultPlanGeneration()
//        householdPlan.generate(person).finish().also { if (index % 100 == 0) println("Working on person $index done") }
//
//    }
}

fun main() {
    val targets = generateHouseholds(10000).flatMap { it.generatePersons(5) }

    runBlocking {
        targets.generateSchedules()
    }

}