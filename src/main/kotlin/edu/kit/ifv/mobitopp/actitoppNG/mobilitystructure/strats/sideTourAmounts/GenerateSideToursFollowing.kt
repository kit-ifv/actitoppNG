package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModifiablePlannedTourAmounts
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel

class GenerateSideToursFollowing(
    params: PlanGenerationParameters,
    choiceModel: FixedChoiceModel<Int, PreviousDayAlternative> =
        context(params) { successorAmountChoiceModel },

    ) : DefaultSideTourDeterminer<SuccessorTourAmountSet>( choiceModel) {

    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours
    }


    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.successorAmount = result
    }

}
