package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.test.assertEquals

class CompareAgainstTestData {
    @TestFactory
    fun compareAgainstTestData(): Collection<DynamicTest> {
        val path = Path("src/test/resources/testPlans.json")
        val expected: List<HouseholdPlanOutput> = Json.decodeFromString(path.readText())
        val actual = generatePlans()
        return expected.zip(actual).withIndex().map { (i, element) ->
            DynamicTest.dynamicTest("Household $i") {
                assertEquals(element.first, element.second)
            }
        }


    }

    private fun generatePlans(): List<HouseholdPlanOutput> {
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
        return plans
    }
}