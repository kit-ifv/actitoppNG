package edu.kit.ifv.mobitopp.actitoppNG.backwardCompatibility

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.generatePersons
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModernizedActivity
import edu.kit.ifv.mobitopp.actitoppNG.randomHouseholds

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.writeText

internal fun generatePlans(): List<HouseholdPlanOutput> {
    val targets = randomHouseholds(1000)
    targets.forEach {
        it.generatePersons(5)
    }
    val params = PlanGenerationParameters()
    val models = AllChoiceModels.create(params)
    val householdPlan = DefaultPlanGeneration(params)
    val plans = targets.map { household ->
        val plans = household.members.associate { member ->
            context(params, models) {
                member.id to householdPlan.generate(member).finish()
            }
        }
        HouseholdPlanOutput(household.id, plans)
    }
    return plans
}

/**
 * The json file to write generated mobility plans to.
 */
internal val EXPORT_TEST_RUN_LOCATION = Path("src/test/resources/testRuns/exposeChoiceModelParams-snapshot-2026-05-20.json")

/**
 * Generates households and writes them to `EXPORT_TEST_RUN_LOCATION`
 *
 * Test with the main in `CompareAgainstTestData.kt`
 */
fun main() {
    val plans = generatePlans()
    EXPORT_TEST_RUN_LOCATION.writeText(Json.encodeToString(plans))
}

@Serializable
internal data class HouseholdPlanOutput(
    val householdID: Int,
    val plans: Map<Int, List<ModernizedActivity>>
)
