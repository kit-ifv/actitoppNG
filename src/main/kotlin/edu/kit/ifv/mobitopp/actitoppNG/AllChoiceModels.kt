package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.activityAmountChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel

data class AllChoiceModels(
    val params: PlanGenerationParameters,
    // week routine
    val activityAmountChoiceModel_: FixedChoiceModel<Int, PersonAlternative> = context(params) { activityAmountChoiceModel }
)