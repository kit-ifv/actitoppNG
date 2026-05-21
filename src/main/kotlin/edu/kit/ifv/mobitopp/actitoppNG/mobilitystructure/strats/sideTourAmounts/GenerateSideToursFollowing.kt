package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModifiablePlannedTourAmounts
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel

class GenerateSideToursFollowing(
    models: AllChoiceModels,
    choiceModel: FixedChoiceModel<Int, PreviousDayAlternative> = models.successorAmountChoiceModel
    ) : DefaultSideTourDeterminer<SuccessorTourAmountSet>( choiceModel) {

    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours
    }


    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.successorAmount = result
    }

}
