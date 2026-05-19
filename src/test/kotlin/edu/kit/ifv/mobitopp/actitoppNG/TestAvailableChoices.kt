package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.tourMainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.generateWeekRoutine
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.collections.minus
import kotlin.random.Random

class TestAvailableChoices {

    /**
     * check whether changed choice models have the expected choices, not more, not less.
     */
    @Test
    fun allPresentTest() {
        context(PlanGenerationParameters()) {
            Assertions.assertEquals(ActivityType.entries.toSet(),  mainActivityChoiceModel.choices)

            Assertions.assertEquals(
                ((ActivityType.entries) - ActivityType.HOME).toSet(),
                tourMainActivityChoiceModel.choices
            )

            Assertions.assertEquals(
                ((ActivityType.entries) - ActivityType.HOME).toSet(),
                sideActivityChoiceModel.choices
            )

            Assertions.assertEquals(precursorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))

            Assertions.assertEquals(successorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))

            Assertions.assertEquals(step5AWithParams.choices, setOf(0, 1, 2, 3, 4, 5))

            Assertions.assertEquals(successorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))
        }
    }


    @Test
    fun testGenerateUtil() {
        val rng = Random(0)
        val person = randomHousehold(rng).generatePersons(1, rng)[0]
        context(PlanGenerationParameters(), rng, AllChoiceModels(PlanGenerationParameters())) {
            val weekRoutine = person.generateWeekRoutine()
            val mobilityStructure = MobilityStructure(person, weekRoutine)
            val nextDay = mobilityStructure.nextDay()
            val dayAlternative = DayAlternative(mobilityStructure.weekRoutine, nextDay.weekday)
            context(dayAlternative) {
                val multiAssign = mainActivityChoiceModel
                val util = multiAssign.utilities()
                assert(util[ActivityType.WORK]!! != 0.0)
                assert(util[ActivityType.EDUCATION]!! != 0.0)
                assert(util[ActivityType.SHOPPING]!! != 0.0)
                assert(util[ActivityType.LEISURE]!! != 0.0)
                assert(util[ActivityType.SHOPPING]!! != 0.0)
                assert(util[ActivityType.TRANSPORT]!! != 0.0)
                assert(util[ActivityType.HOME]!! == 0.0)
            }
        }
    }
}