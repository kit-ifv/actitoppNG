package edu.kit.ifv.mobitopp

import edu.kit.ifv.mobitopp.actitopp.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.ModernizedActivity
import kotlin.random.Random


private val random = Random(1)
private fun generateHousehold(): ActiToppHousehold {
    return ActiToppHousehold(
        random.nextInt(0, 10),
        random.nextInt(0, 10),
        random.nextInt(0, 10),
        random.nextInt(0, 10)
    )
}

fun generateHouseholds(amount: Int): List<ActiToppHousehold> {
    return (0..<amount).map { generateHousehold() }
}

fun ActiToppHousehold.generatePersons(amount: Int): List<ActitoppPerson> {
    return (0..<amount).map {
        this.generatePerson(it)
    }
}

fun ActiToppHousehold.generatePerson(number: Int): ActitoppPerson {
    return ActitoppPerson(
        household = this,
        persNrinHousehold = number,
        age = random.nextInt(0, 100),
        employmentCode = random.nextInt(0, 42),
        genderCode = random.nextInt(0, 3),
        commutingdistance_work = random.nextDouble(),
        commutingdistance_education = random.nextDouble()
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