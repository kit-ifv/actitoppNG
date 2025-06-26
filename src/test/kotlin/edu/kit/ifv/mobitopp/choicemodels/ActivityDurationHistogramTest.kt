package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.changes.Category
import edu.kit.ifv.mobitopp.actitopp.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.LEAD
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.MINOR
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.OTHER_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.choicemodels.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.choicemodels.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.randomMobilityPlanInput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DynamicTest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.test.assertEquals
private val RESOURCE_PATH = Path("src/test/resources/choicemodels")
abstract class ActivityDurationHistogramTest(activityDurationHistograms: ActivityDurationHistograms<*>):
    ChoiceModelTest<ArrayHistogram, MainDurationAlternative>(activityDurationHistograms.choiceModel) {
    override val serializer: KSerializer<ArrayHistogram> = ArrayHistogramIdOnlySerializer

    override fun converter(option: ArrayHistogram): MainDurationAlternative {
        return MainDurationAlternative(option, randomMobilityPlanInput(inputRandom))
    }

    override fun compareAgainstData(): Collection<DynamicTest> {
        val path = RESOURCE_PATH.resolve(name)
        val expected: List<ArrayHistogram> = Json.decodeFromString(listSerializer, path.readText())
        return expected.withIndex().map { (i, element) ->
            DynamicTest.dynamicTest("$name $i") {
                assertEquals(element.categoryIndex, choiceModel.select(randomNumber = selectRandom.nextDouble(), ::converter).categoryIndex)
            }

        }
    }
}

class MinorTest: ActivityDurationHistogramTest(MINOR) {
    override val name: String = "minorDurationTest"
}

class MajorTest: ActivityDurationHistogramTest(MAJOR) {
    override val name: String = "majorDurationTest"
}

class LeadTest: ActivityDurationHistogramTest(LEAD) {
    override val name: String = "leadDurationTest"
}

class FirstStartTest: ActivityDurationHistogramTest(FIRST_TOUR_HISTOGRAM) {
    override val name: String = "firstStartTest"
}

class SecondStartTest: ActivityDurationHistogramTest(SECOND_TOUR_HISTOGRAM) {
    override val name: String = "secondStartTest"
}

class OtherStartTest: ActivityDurationHistogramTest(OTHER_TOUR_HISTOGRAM) {
    override val name: String = "otherStartTest"
}
object ArrayHistogramIdOnlySerializer : KSerializer<ArrayHistogram> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ArrayHistogram", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ArrayHistogram) {
        encoder.encodeString(value.categoryIndex.toInt().toString()) // only serialize the id
    }

    override fun deserialize(decoder: Decoder): ArrayHistogram {
        val id = decoder.decodeString()
        return ArrayHistogram(offset = 0, values = emptyList(), categoryIndex = Category(id.toInt())) // dummy data
    }
}