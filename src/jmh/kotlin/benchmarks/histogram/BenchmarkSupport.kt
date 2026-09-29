package benchmarks.histogram

import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomMobilityPlanInput
import kotlin.random.Random

/**
 * Everything that both benchmark mechanisms need.
 *
 * MAJOR is deliberately not in here because its concrete type does not
 * need to leak into this shared structure.
 */
internal data class DurationSelectionFixture(
    val alternative: MainDurationAlternative,
)

/**
 * Creates the actual workload used by BOTH the profiler and JMH.
 *
 * Anything that changes what is being benchmarked should normally
 * happen here rather than in either benchmark frontend.
 */
internal fun createDurationSelectionFixture(seed: Int = 1234): DurationSelectionFixture {
    val mobilityPlanInputs =
        randomMobilityPlanInput(Random(seed))

    val alternative =
        MainDurationAlternative(mobilityPlanInputs)

    return DurationSelectionFixture(
        alternative = alternative,
    )
}