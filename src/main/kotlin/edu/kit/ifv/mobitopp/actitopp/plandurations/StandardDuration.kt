package edu.kit.ifv.mobitopp.actitopp.plandurations

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.SealedDiscreteChoiceModel


fun interface StandardDuration {
    fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean
}

class UtilityFunctionAssignment(
    val rngHelper: RNGHelper,
    private val choiceModel: SealedDiscreteChoiceModel<Boolean, BooleanDecisionSituation> = firstActivityUsesStandardDuration,
) : StandardDuration {
    override fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean {
        val randomNumber = rngHelper.randomValue

        val converter: (Boolean) -> BooleanDecisionSituation = {
            BooleanDecisionSituation(it, input)
        }

        return choiceModel.select(randomNumber, converter)
    }

}

