package edu.kit.ifv.mobitopp.actitopp

import edu.kit.ifv.mobitopp.actitopp.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitopp.modernization.ModernizedActivity
import edu.kit.ifv.mobitopp.actitopp.modernization.StandardHouseholdPlan
import edu.kit.ifv.mobitopp.generateHouseholds
import edu.kit.ifv.mobitopp.generatePerson
import edu.kit.ifv.mobitopp.generatePersons
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun main() {
    val targets = generateHouseholds(10)
    targets.forEach {

        it.generatePersons(5)

    }

    val householdPlan = DefaultPlanGeneration()
    val plans = targets.map { household ->
        val plans = household.members.associate { member ->
            member.id to householdPlan.generate(member).finish()
        }
        HouseholdPlanOutput(household.id, plans)
    }
    println(Json.encodeToString(plans))
}

@Serializable
data class HouseholdPlanOutput(
    val householdID: Int,
    val plans: Map<Int, List<ModernizedActivity>>
)
