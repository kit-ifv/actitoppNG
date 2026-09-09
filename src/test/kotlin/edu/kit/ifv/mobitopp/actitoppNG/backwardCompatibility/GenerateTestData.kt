package edu.kit.ifv.mobitopp.actitoppNG.backwardCompatibility

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.generatePersons
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DefaultPlanGeneration
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModernizedActivity
import edu.kit.ifv.mobitopp.actitoppNG.randomHouseholds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.writeText


/**
 * The json file to write generated mobility plans to.
 * This is the val that gets tested against and written to in [CompareAgainstTestData](./CompareAgainstTestData.kt) and
 * [GenerateTestData.kt](./GenerateTestData.kt)
 */
internal val SELECTED_RUN: Runs = Runs.`2026-09-03-Households-50k`

const val snapshotDir = "src/test/resources/testRuns/"

@Suppress("unused", "EnumEntryName")
enum class Runs(val numHouseholds: Int, val path: Path) {
    `2026-05-20-Households-10k`(10_000, Path(snapshotDir, "exposeChoiceModelParams-snapshot-2026-05-20.json")),
    `2026-09-03-Households-10k`(10_000, Path(snapshotDir,"ChoiceModelRework-snapshot-10k-households-2026-09-03.json")),
    `2026-09-03-Households-50k`(50_000, Path(snapshotDir,"ChoiceModelRework-snapshot-50k-households-2026-09-03.json")),
    ;
}

/**
 * If you compare against previous runs, ensure they have the same number of households.
 */
internal fun generatePlans(numHouseHolds: Int = 50000): List<HouseholdPlanOutput> {
    val targets = randomHouseholds(numHouseHolds)
    targets.forEach {
        it.generatePersons(5)
    }
    val models = AllChoiceModels.DEFAULT
    val householdPlan = DefaultPlanGeneration(models)
    val plans = targets.parallelStream().map { household ->
        val plans = household.members.associate { member ->
            context(models) {
                member.id to householdPlan.generate(member).finish()
            }
        }
        HouseholdPlanOutput(household.id, plans)
    }.toList()
    return plans
}

/**
 * Generates households and writes them to [SELECTED_RUN]. If a snapshot already exists, asks for permission.
 *
 * Test with the main in `CompareAgainstTestData.kt`
 */
fun main() {
    if(SELECTED_RUN.path.exists()) {
        if (getUserConfirmation()) println("Overwriting the existing run.")
        else return println("Ok, we will not overwrite")
    }

    println("Generating ${SELECTED_RUN.numHouseholds} households...")
    val plans = generatePlans()
    SELECTED_RUN.path.deleteIfExists()
    SELECTED_RUN.path.writeText(Json.encodeToString(plans))
    println("Done.\n")
    println("Note: Snapshots can be large and should be tracked with git lfs. If you did not initialize git lfs " +
            "on your machine, do so by installing the git lfs package and initialize it with `git lfs install` (see https://docs.github.com/en/repositories/working" +
            "-with-files/managing-large-files/installing-git-large-file-storage?platform=windows for windows)." +
            "\nThen just start tracking the file with `git lfs track \"path/to/the/snapshot_file.json\"` and commit the file together with the .gitattributes file. The rest " +
            "should work just like any other git tracked files.")

}

/**
 * @return true if user wants to overwrite the existing run. False if not.
 */
fun getUserConfirmation(): Boolean {
    println("There already exists a snapshot file for the given run. Are you sure you want to overwrite it? (Y/n)")
    var userDecision = System.`in`.bufferedReader().readLine()
    var withoutSpaces = userDecision.replace(" ", "")
    while (withoutSpaces != "Y") {
        if(withoutSpaces == "n") {
            return false
        }
        println("There already exists a snapshot file for the given run. Are you sure you want to overwrite it? (Y/n)")
        userDecision = System.`in`.bufferedReader().readLine()
        withoutSpaces = userDecision.replace(" ", "")
    }
    return true
}

@Serializable
internal data class HouseholdPlanOutput(
    val householdID: Int,
    val plans: Map<Int, List<ModernizedActivity>>
)
