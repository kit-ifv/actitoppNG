package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiablePlannedTourAmounts

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
