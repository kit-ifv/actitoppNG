package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.subsubTourAmount

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel

abstract class SideTourActivityGenerator<P>(
    val rngHelper: RNGHelper,
    val choiceModel: FixedChoiceModel<Int, TourAlternativeInt>,
) : GenerateSideTourActivities {
    override fun generateActivityAmount(input: SideTourActivityInput): Int {

        val options = choiceModel.choices.toMutableSet()
        options.removeIf { it < input.minimumAmountOfActivities }

        val converter: (Int) -> TourAlternativeInt = {
            TourAlternativeInt(

                input.person,
                input.routine,
                input.currentDay,
                input.tour,
                input.amountOfActivitiesBeforeMainAct
            )
        }
        return context(
            TourAlternativeInt(
                input.person,
                input.routine,
                input.currentDay,
                input.tour,
                input.amountOfActivitiesBeforeMainAct
            ), rngHelper
        ) {
            choiceModel.select(options)
        }
    }
}

class PrecedingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourPrecursorSet>(
    rngHelper, step5AWithParams,
)

class FollowingSpawns(rngHelper: RNGHelper) : SideTourActivityGenerator<SideTourSuccessorSet>(
    rngHelper, step5BWithParams,
)