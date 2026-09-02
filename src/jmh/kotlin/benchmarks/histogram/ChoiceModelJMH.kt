package benchmarks.histogram

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Threads
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random


@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(
    iterations = 5,
    time = 1,
    timeUnit = TimeUnit.SECONDS,
)
@Measurement(
    iterations = 10,
    time = 1,
    timeUnit = TimeUnit.SECONDS,
)
@Fork(3)
@Threads(1)
open class SingularDurationSelectionBenchmark {
    @Param(
        "1001",
        "2002",
        "3003",
        "4004",
        "5005",
    )
    var inputSeed: Int = 0
    private lateinit var fixture:
            DurationSelectionFixture

    private lateinit var random:
            Random


    /*
     * Outside the measured region.
     *
     * We don't have to name MAJOR's actual concrete type because Kotlin
     * infers it for this private property.
     */
    private val major = context(PlanGenerationParameters()) {
        MAJOR
    }


    @Setup(Level.Trial)
    fun setupTrial() {
        fixture =
            createDurationSelectionFixture(inputSeed)
    }


    /**
     * Each JMH iteration gets a fresh identical random sequence.
     *
     * Level.Invocation would be disastrous here because it would add
     * Random construction to every measured invocation.
     */
    @Setup(Level.Iteration)
    fun setupIteration() {
        random =
            Random(42)
    }


    @Benchmark
    fun invariantArrayChoiceModel(
        blackhole: Blackhole,
    ) {
        context(
            random,
            fixture.alternative,
        ) {
            blackhole.consume(
                fixture.invariantModel.select()
            )
        }
    }


    @Benchmark
    fun majorSelectSingular(
        blackhole: Blackhole,
    ) {
        context(
            random,
            fixture.alternative,
        ) {
            blackhole.consume(
                major.selectSingular(
                    fixture.alternative
                )
            )
        }
    }
}