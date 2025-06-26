package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.subsubTourAmount

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.TourAlternativeInt

abstract class SideTourActivityGenerator<P>(
    val rngHelper: RNGHelper,
    val choiceModel: EnumeratedDiscreteChoiceModel<Int, TourAlternativeInt, P>,
) : GenerateSideTourActivities {
    override fun generateActivityAmount(input: SideTourActivityInput): Int {

        val options = choiceModel.choices.toMutableSet()
        options.removeIf { it < input.minimumAmountOfActivities }

        val converter: (Int) -> TourAlternativeInt = {
            TourAlternativeInt(
                it,
                input.person,
                input.routine,
                input.currentDay,
                input.tour,
                input.amountOfActivitiesBeforeMainAct
            )
        }
        return choiceModel.select(options, rngHelper, converter)
    }
}

class PrecedingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourPrecursorSet>(
    rngHelper, step5AWithParams,
)

class FollowingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourSuccessorSet>(
    rngHelper, step5BWithParams,
)