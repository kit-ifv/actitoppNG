package edu.kit.ifv.mobitopp.choicemodels


import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.random.Random
import kotlin.test.assertEquals

private val RESOURCE_PATH = Path("src/test/resources/choicemodels")
abstract class ChoiceModelTest<X : Any, SIT >(
    protected val choiceModel: FixedChoiceModel<X, SIT>
) {
    // Rng to create the input data for the test runs
    protected val inputRandom = Random(1)

    // Rng for the selection
    protected val selectRandom = Random(1)
    abstract val name: String
    abstract fun converter(): SIT
    abstract val serializer: KSerializer<X>
    val listSerializer by lazy {  ListSerializer(serializer)}
    fun writeResults() {

        val output = context(converter(), selectRandom) {
             (0..<1000).map { choiceModel.select() }
        }


        val jsonstring = Json.encodeToString(listSerializer, output)
        RESOURCE_PATH.resolve(name).writeText(jsonstring)
    }

    @TestFactory
    open fun compareAgainstData(): Collection<DynamicTest> {
        val path = RESOURCE_PATH.resolve(name)
        val expected: List<X> = Json.decodeFromString(listSerializer, path.readText())
        return expected.withIndex().map { (i, element) ->
            DynamicTest.dynamicTest("$name $i") {

                context(converter(), selectRandom) {
                    assertEquals(element, choiceModel.select())
                }

            }

        }
    }
}


fun main() {
    MainActivityTest().writeResults()
    PrecursorAmountTest().writeResults()
    SuccessorAmountTest().writeResults()
    SideActivityTest().writeResults()
    SideTourActivityTest().writeResults()
    SideTourPrecursorAmountTest().writeResults()
    SideTourSuccessorAmountTest().writeResults()
    MinorTest().writeResults()
    MajorTest().writeResults()
    LeadTest().writeResults()
    FirstStartTest().writeResults()
    SecondStartTest().writeResults()
    OtherStartTest().writeResults()
    UseStandardDurationTest().writeResults()
    WorkTest().writeResults()
    EducationTest().writeResults()
    ActivityAmountTest().writeResults()
    ImmobileTest().writeResults()
    LeisureTest().writeResults()
    ServiceTest().writeResults()
    ShoppingTest().writeResults()
    TourAmountTest().writeResults()




}
