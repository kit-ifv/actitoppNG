package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitopp.randomDayStructure
import edu.kit.ifv.mobitopp.actitopp.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.randomPlannedTourAmounts
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class SuccessorAmountTest : ChoiceModelTest<Int, PreviousDayAlternative>(successorAmountChoiceModel) {
    override val name: String = "successorAmountChoiceModel"
    override val serializer: KSerializer<Int> = Int.serializer()

    override fun converter(option: Int): PreviousDayAlternative {
        return PreviousDayAlternative(option,
            day = randomDayStructure(inputRandom),
            previousResults = randomPlannedTourAmounts(inputRandom),
            personWithRoutine = randomPersonWithRoutine(inputRandom),
            plannedPrecursorTours = inputRandom.nextInt(0, 5))
    }
}