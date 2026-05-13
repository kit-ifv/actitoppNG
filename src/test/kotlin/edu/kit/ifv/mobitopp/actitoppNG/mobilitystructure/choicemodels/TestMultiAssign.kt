package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.generatePersons
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomHousehold
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.generateWeekRoutine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class TestMultiAssign {

    @Test
    fun allPresentTest() {
        context(PlanGenerationParameters()) {
            // changed models testing whether all choices are still present:

            val mainActivityCM = mainActivityChoiceModel
            assertEquals(ActivityType.entries.toSet(), mainActivityCM.choices)

            assertEquals(((ActivityType.entries) - ActivityType.HOME).toSet(),
                tourMainActivityChoiceModel.choices)

            assertEquals(((ActivityType.entries) - ActivityType.HOME).toSet(),
                sideActivityChoiceModel.choices)

            assertEquals(precursorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))

            assertEquals(successorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))

            assertEquals(step5AWithParams.choices, setOf(0, 1, 2, 3, 4, 5))
            
            assertEquals(successorAmountChoiceModel.choices, setOf(0, 1, 2, 3, 4, 5))
        }
    }


    @Test
    fun testMultiAssign() {
        val rng = Random(0)
        val person = randomHousehold(rng).generatePersons(1, rng)[0]
        context(PlanGenerationParameters(), rng) {
            val weekRoutine = person.generateWeekRoutine()
            val mobilityStructure = MobilityStructure(person, weekRoutine)
            val nextDay = mobilityStructure.nextDay()
            val dayAlternative: DayAlternative = DayAlternative(mobilityStructure.weekRoutine, nextDay.weekday)
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