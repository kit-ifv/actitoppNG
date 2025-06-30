package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.sideTourAmounts

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.ModifiablePlannedTourAmounts


class GenerateSideToursPreceeding(
    rngHelper: RNGHelper,
    choiceModel: EnumeratedDiscreteChoiceModel<Int, PreviousDayAlternative, PrecursorTourAmountSet> = precursorAmountChoiceModel,
) : DefaultSideTourDeterminer<PrecursorTourAmountSet>(rngHelper, choiceModel) {


    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours / 2
    }


    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.precursorAmount = result
    }
}