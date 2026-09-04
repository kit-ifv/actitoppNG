package edu.kit.ifv.mobitopp.actitoppNG.backwardCompatibility

import kotlinx.serialization.json.Json
import kotlin.io.path.readText
import kotlin.test.assertEquals


internal fun printFirstPlanDiff(expected: HouseholdPlanOutput, actual: HouseholdPlanOutput) {
    val generatedWeeks = (expected.plans.keys + actual.plans.keys).toSet()
    for (weekNumber in generatedWeeks) {
        if (expected.plans[weekNumber]!! != actual.plans[weekNumber]!!) {
            println("Plans in week $weekNumber (from weeks ${generatedWeeks.joinToString()}) don't match.")
            val expectedWeekPlan = expected.plans[weekNumber]!!
            val actualWeekPlan = actual.plans[weekNumber]!!
            expectedWeekPlan.zip(actualWeekPlan).forEachIndexed { i, (activityExpected, actualActivity) ->
                if (activityExpected != actualActivity) {
                    println("\nActivity with index $i does not match.")
                    println("Expected: $activityExpected \nActual: $actualActivity")
                }
            }
        }
    }
}

/**
 * Test the current build against a previous build. The previous data is taken from the file at
 * `EXPORT_TEST_RUN_LOCATION`.
 * Use this for example for ensuring changes that should not change behavior do not change behavior.
 * This compares the entire plan, not just the ids.
 */
fun main() {
    val expected: List<HouseholdPlanOutput> = Json.decodeFromString(EXPORT_TEST_RUN_LOCATION.readText())
    val actual = generatePlans()
    expected.zip(actual).forEachIndexed { i, (expectedHH, generatedHH) ->
        println("Household $i")
        if (expectedHH != generatedHH) {
            if (expectedHH.householdID != generatedHH.householdID) {
                println("Household IDs are not equal...? testID: ${expectedHH.householdID}; generatedID: ${generatedHH.householdID}")
            }
            if (expectedHH.plans.keys != generatedHH.plans.keys) {
                println("The generated household is missing or has more weeks generated than the test household.")
            }
            if (expectedHH.plans != generatedHH.plans) {
                printFirstPlanDiff(expectedHH, generatedHH)
            }
        }

        assertEquals(expectedHH, generatedHH,
            "Household $i are not equal. \n\nexpected: ${expectedHH} \nactual: ${generatedHH}\n\n"
        )
    }
}

