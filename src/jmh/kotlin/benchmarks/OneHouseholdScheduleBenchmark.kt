package benchmarks

import edu.kit.ifv.mobitopp.actitoppNG.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitoppNG.ActitoppPerson
import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PersonAttributes
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ReusablePlanGeneration
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 8)
@Threads(1)
@Fork(
    value = 1,
    jvmArgsAppend = [
        "-Xms4g",
        "-Xmx4g",
        "-XX:+UnlockDiagnosticVMOptions",
        "-XX:+DebugNonSafepoints"
    ]
)
open class OneHouseholdScheduleBenchmark {


    private lateinit var persons: List<ActitoppPerson>
    private lateinit var params: PlanGenerationParameters
    private lateinit var models: AllChoiceModels
    private lateinit var executionContext: ReusablePlanGeneration

    @Setup(Level.Trial)
    fun setup() {
        params = PlanGenerationParameters()
        models = AllChoiceModels.create(params)
        executionContext = ReusablePlanGeneration(models)

        val household = randomHouseholds(1).single()
        persons = household.generatePersons(5)
    }


    @Benchmark
    fun generateOneHouseholdRepeated(bh: Blackhole) {

        persons.forEach {
            context(params, models) {
                bh.consume(executionContext.generate(it).finish())
            }

        }

    }
}

private val default = Random(1)
fun randomHousehold(random: Random = default): ActiToppHousehold {
    return ActiToppHousehold(
        random.nextInt(0, 10),
        random.nextInt(0, 10),
        AreaType.fromCode(random.nextInt(0, 10)),
        random.nextInt(0, 10)
    )
}

fun randomHouseholds(amount: Int, random: Random = default): List<ActiToppHousehold> {
    return (0..<amount).map { randomHousehold(random) }
}

fun ActiToppHousehold.generatePersons(amount: Int, random: Random = default): List<ActitoppPerson> {
    return (0..<amount).map {
        this.randomPerson(random)
    }
}

fun ActiToppHousehold.randomPerson(random: Random = default): ActitoppPerson {
    return ActitoppPerson(
        household = this,
        attributes = PersonAttributes.random(random),
    )
}
