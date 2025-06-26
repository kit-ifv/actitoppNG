package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiablePlannedTourAmounts


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