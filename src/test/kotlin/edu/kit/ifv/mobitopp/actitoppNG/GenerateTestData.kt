package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModernizedActivity

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.writeText

fun main() {
    val targets = randomHouseholds(1000)
    targets.forEach {

        it.generatePersons(5)

    }

    val params = PlanGenerationParameters()
    val models = AllChoiceModels(params)
    val householdPlan = DefaultPlanGeneration(params)
    val plans = targets.map { household ->
        val plans = household.members.associate { member ->
            context(params, models) {
                member.id to householdPlan.generate(member).finish()
            }
        }
        HouseholdPlanOutput(household.id, plans)
    }
    val path = Path("src/test/resources/testPlans.json")
    path.writeText(Json.encodeToString(plans))

}

@Serializable
data class HouseholdPlanOutput(
    val householdID: Int,
    val plans: Map<Int, List<ModernizedActivity>>
)
