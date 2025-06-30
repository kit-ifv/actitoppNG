package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomDayStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.randomPlannedTourAmounts
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class PrecursorAmountTest: ChoiceModelTest<Int, PreviousDayAlternative>(precursorAmountChoiceModel) {
    override val name: String = "precursorAmountChoiceModel"
    override val serializer: KSerializer<Int> = Int.serializer()

    override fun converter(option: Int): PreviousDayAlternative {
        return PreviousDayAlternative(option,
            day = randomDayStructure(inputRandom),
            previousResults = randomPlannedTourAmounts(inputRandom),
            personWithRoutine = randomPersonWithRoutine(inputRandom),
            plannedPrecursorTours = inputRandom.nextInt(0, 5))
    }
}