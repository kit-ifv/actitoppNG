package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.randomDayStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class SideTourPrecursorAmountTest: ChoiceModelTest<Int, TourAlternativeInt>(step5AWithParams) {
    override val name: String = "sideTourPrecursorAmountTest"
    override val serializer: KSerializer<Int> = Int.serializer()

    override fun converter(option: Int): TourAlternativeInt {
        val randomDayStructure = randomDayStructure(inputRandom)
        val (person, routine) = randomPersonWithRoutine(inputRandom)
        return TourAlternativeInt(option, person, routine, randomDayStructure, randomDayStructure.indexedElements().random(inputRandom),        amountOfPrecursorActivities = inputRandom.nextInt(0, 5), )

    }
}

fun main()  {
    SideTourPrecursorAmountTest().writeResults()
}