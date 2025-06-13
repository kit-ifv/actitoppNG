package edu.kit.ifv.mobitopp

import edu.kit.ifv.mobitopp.actitopp.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.enums.AreaType
import edu.kit.ifv.mobitopp.actitopp.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.ModernizedActivity
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

fun Collection<ActitoppPerson>.generateSchedules(): List<List<ModernizedActivity>> {
    return withIndex().map { (index, person) ->
        val householdPlan = DefaultPlanGeneration()
        householdPlan.generate(person).finish().also { if (index % 100 == 0) println("Working on person $index done") }

    }
}

fun main() {
    val targets = generateHouseholds(1000).flatMap { it.generatePersons(5) }


    targets.generateSchedules()
}