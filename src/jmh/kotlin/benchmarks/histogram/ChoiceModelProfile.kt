package benchmarks.histogram

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR_NEW
import kotlin.random.Random


private const val WARMUP_REPETITIONS = 1_000_000
private const val PROFILE_REPETITIONS = 20_000_000


@Volatile
private var profilerBlackhole: Int = 0


private fun consumeForProfiler(
    value: Any?,
    accumulator: Int,
): Int {
    val hash =
        if (value == null) {
            0
        } else {
            System.identityHashCode(value)
        }

    return accumulator * 31 + hash
}

/**
 * This is meant for profiling. Add "major" or "major_new" to the program run arguments in the run configuration to either test the new or old
 * choicemodel.
 */
fun main(args: Array<String>) {

    val implementation =
        args.firstOrNull()?.lowercase() ?: "major"

    require(
        implementation == "major" || implementation == "major_new"
    ) {
        "Expected 'major' or 'major_new', got '$implementation'"
    }

    val fixture =
        createDurationSelectionFixture()

    val major = context(PlanGenerationParameters()) {
        MAJOR
    }

    val majorNew = context(PlanGenerationParameters()) {
        MAJOR_NEW
    }
    var accumulator = 0

    /*
     * Warm up ONLY the implementation we're going to profile.
     */
    when (implementation) {
        "major_new" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(WARMUP_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            majorNew.selectSingular(
                                fixture.alternative
                            ),
                            accumulator,
                        )
                }
            }
        }
        "major" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(WARMUP_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            major.selectSingular(
                                fixture.alternative
                            ),
                            accumulator,
                        )
                }
            }
        }
    }

    profilerBlackhole = accumulator


    /*
     * Start the interesting workload with the RNG reset.
     */
    accumulator = 0

    when (implementation) {
        "major_new" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(PROFILE_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            majorNew.selectSingular(
                                fixture.alternative
                            ),
                            accumulator,
                        )
                }
            }
        }

        "major" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(PROFILE_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            major.selectSingular(
                                fixture.alternative
                            ),
                            accumulator,
                        )
                }
            }
        }
    }

    profilerBlackhole =
        profilerBlackhole xor accumulator

    println(
        "Used configuration=$implementation, " +
                "blackhole=$profilerBlackhole"
    )
    println("Ran $WARMUP_REPETITIONS warmup selections")
    println("Ran $PROFILE_REPETITIONS actual selections")
}