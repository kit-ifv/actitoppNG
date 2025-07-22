package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomDayStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.randomPlannedTourAmounts
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class SuccessorAmountTest : ChoiceModelTest<Int, PreviousDayAlternative>(successorAmountChoiceModel) {
    override val name: String = "successorAmountChoiceModel"
    override val serializer: KSerializer<Int> = Int.serializer()

    override fun converter(): PreviousDayAlternative {
        return PreviousDayAlternative(
            day = randomDayStructure(inputRandom),
            previousResults = randomPlannedTourAmounts(inputRandom),
            personWithRoutine = randomPersonWithRoutine(inputRandom),
            plannedPrecursorTours = inputRandom.nextInt(0, 5))
    }
}