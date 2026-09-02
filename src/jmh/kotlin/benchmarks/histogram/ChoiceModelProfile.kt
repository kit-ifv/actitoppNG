package benchmarks.histogram

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
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


fun main(args: Array<String>) {

    val implementation =
        args.firstOrNull()?.lowercase() ?: "build"

    require(
        implementation == "build" ||
                implementation == "major"
    ) {
        "Expected 'build' or 'major', got '$implementation'"
    }

    val fixture =
        createDurationSelectionFixture()

    val major = context(PlanGenerationParameters()) {
        MAJOR
    }

    var accumulator = 0

    /*
     * Warm up ONLY the implementation we're going to profile.
     */
    when (implementation) {

        "build" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(WARMUP_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            fixture.invariantModel.select(),
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

        "build" -> {
            context(
                Random(42),
                fixture.alternative,
            ) {
                repeat(PROFILE_REPETITIONS) {
                    accumulator =
                        consumeForProfiler(
                            fixture.invariantModel.select(),
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
        "implementation=$implementation, " +
                "blackhole=$profilerBlackhole"
    )
}