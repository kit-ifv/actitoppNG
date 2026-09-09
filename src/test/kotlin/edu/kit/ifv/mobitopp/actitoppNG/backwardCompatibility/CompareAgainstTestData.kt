package edu.kit.ifv.mobitopp.actitoppNG.backwardCompatibility

import kotlinx.serialization.json.Json
import kotlin.io.path.name
import kotlin.io.path.notExists
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
 * Test the current build compared against a previous build. The previous data is taken from the file at
 * [SELECTED_RUN].
 * Use this for example for ensuring changes that should not change behavior actually do not change behavior.
 * This compares the entire plan, with the activities, lengths etc.
 */
fun main() {
    if (SELECTED_RUN.path.notExists()) {
        println("The selected run has no snapshot file. Generate test data first with GenerateTestData.kt or check out " +
                "the correct revision with the desired file.")
    }
    var expected: List<HouseholdPlanOutput> = emptyList()
    try {
        expected = Json.decodeFromString(SELECTED_RUN.path.readText())
    } catch (e: Exception) {
        println("Failed to read the snapshot json file. It may be corrupted in some way.")
        println("Note, that snapshots are (and should be) tracked with git lfs. If you did not initialize it" +
                "on your machine, do so with `git lfs install` and pull the branch again. You can see that it worked" +
                "by looking into the file. If the file contains some git related text and hashes, it is still the " +
                "placeholder and you need to ensure git lfs is initialized properly again. For windows you need to " +
                "install git lfs first. See [Windows Install](https://docs.github.com/en/repositories/working-with-files/managing-large-files/installing-git-large-file-storage?platform=windows) ")
        println("Error message: ${e.message}")
        return
    }
    val actual = generatePlans()
    expected.zip(actual).forEachIndexed { i, (expectedHH, generatedHH) ->
        println("Comparing Household $i")
        if (expectedHH != generatedHH) {
            println("❌ Household $i not equal snapshot household.")
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
    println("\nDone.\n✅ No differences found between this build and ${SELECTED_RUN.path.fileName.name}")
}

