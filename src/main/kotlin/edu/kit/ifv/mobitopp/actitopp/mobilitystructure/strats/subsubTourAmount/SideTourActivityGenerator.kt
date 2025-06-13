package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.subsubTourAmount

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.TourSituationInt
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel

abstract class SideTourActivityGenerator<P>(
    val rngHelper: RNGHelper,
    val choiceModel: ParametrizedDiscreteChoiceModel<Int, TourSituationInt, P>,
) : GenerateSideTourActivities {
    override fun generateActivityAmount(input: SideTourActivityInput): Int {

        val options = choiceModel.registeredOptions().toMutableSet()
        options.removeIf { it < input.minimumAmountOfActivities }

        val converter: (Int) -> TourSituationInt = {
            TourSituationInt(
                it,
                input.person,
                input.routine,
                input.currentDay,
                input.tour,
                input.amountOfActivitiesBeforeMainAct
            )
        }
        val rnd = rngHelper.randomValue
        return choiceModel.select(options, rnd, converter)
    }
}

class PrecedingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourPrecursorSet>(
    rngHelper, step5AWithParams,
)

class FollowingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourSuccessorSet>(
    rngHelper, step5BWithParams,
)