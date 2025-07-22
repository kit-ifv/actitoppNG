package edu.kit.ifv.mobitopp.actitoppNG.plandurations

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel


fun interface StandardDuration {
    fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean
}

class UtilityFunctionAssignment(
    val rngHelper: RNGHelper,
    private val choiceModel: FixedChoiceModel<Boolean, BooleanDecisionAlternative> = firstActivityUsesStandardDuration,
) : StandardDuration {
    override fun getAssignedStandardDuration(input: MobilityPlanInputs): Boolean {

        val converter: (Boolean) -> BooleanDecisionAlternative = {
            BooleanDecisionAlternative(input)
        }
        return context(BooleanDecisionAlternative(input), rngHelper) {
            choiceModel.select()
        }
    }

}

