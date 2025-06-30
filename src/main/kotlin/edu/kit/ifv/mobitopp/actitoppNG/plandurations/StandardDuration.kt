package edu.kit.ifv.mobitopp.actitoppNG.plandurations

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitoppNG.utilityFunctions.select


fun interface StandardDuration {
    fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean
}

class UtilityFunctionAssignment(
    val rngHelper: RNGHelper,
    private val choiceModel: EnumeratedDiscreteChoiceModel<Boolean, BooleanDecisionAlternative, *> = firstActivityUsesStandardDuration,
) : StandardDuration {
    override fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean {

        val converter: (Boolean) -> BooleanDecisionAlternative = {
            BooleanDecisionAlternative(it, input)
        }

        return choiceModel.select(rngHelper, converter)
    }

}

