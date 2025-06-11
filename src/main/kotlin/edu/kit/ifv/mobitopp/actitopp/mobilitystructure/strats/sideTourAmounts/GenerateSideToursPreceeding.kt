package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.HTour
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiablePlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel


class GenerateSideToursPreceeding(rngHelper: RNGHelper,
                                  choiceModel: ParametrizedDiscreteChoiceModel<Int, PreviousDaySituation, PrecursorTourAmountSet> = precursorAmountChoiceModel,
) : DefaultSideTourDeterminer<PrecursorTourAmountSet>(rngHelper, choiceModel) {


    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours / 2
    }

    override fun spawnTour(day: HDay): HTour {
        return day.generatePrecedingTour()
    }

    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.precursorAmount = result
    }
}