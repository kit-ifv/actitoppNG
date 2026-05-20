package edu.kit.ifv.mobitopp.actitoppNG.backwardCompatibility

import kotlinx.serialization.json.Json
import kotlin.io.path.readText
import kotlin.test.assertEquals


/**
 * Test the current build against a previous build. The previous data is taken from the file at
 * `EXPORT_TEST_RUN_LOCATION`.
 * Use this for example for ensuring changes that should not change behavior do not change behavior.
 */
fun main() {
    val expected: List<HouseholdPlanOutput> = Json.decodeFromString(EXPORT_TEST_RUN_LOCATION.readText())
    val actual = generatePlans()
    expected.zip(actual).withIndex().map { (i, element) ->
        println("Household $i")
        assertEquals(element.first, element.second,
            "Household $i are not equal. \nexpected: ${element.first} \nactual: ${element.second}"
        )
    }
}