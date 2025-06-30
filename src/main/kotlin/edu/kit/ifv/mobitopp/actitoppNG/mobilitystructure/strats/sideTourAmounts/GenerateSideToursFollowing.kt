package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.sideTourAmounts

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModifiablePlannedTourAmounts

class GenerateSideToursFollowing(
    rngHelper: RNGHelper,
    choiceModel: EnumeratedDiscreteChoiceModel<Int, PreviousDayAlternative, SuccessorTourAmountSet> = successorAmountChoiceModel,

    ) : DefaultSideTourDeterminer<SuccessorTourAmountSet>(rngHelper, choiceModel) {

    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours
    }


    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.successorAmount = result
    }

}
