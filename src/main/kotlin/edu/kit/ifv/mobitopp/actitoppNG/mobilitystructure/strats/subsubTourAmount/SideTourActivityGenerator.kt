package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.subsubTourAmount

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import kotlin.random.Random

abstract class SideTourActivityGenerator<P>(
    val choiceModel: FixedChoiceModel<Int, TourAlternativeInt>,
) : GenerateSideTourActivities {
    context(rng: Random)
    override fun generateActivityAmount(input: SideTourActivityInput): Int {

        return context(
            TourAlternativeInt(
                input.person,
                input.routine,
                input.currentDay,
                input.tour,
                input.amountOfActivitiesBeforeMainAct
            )
        ) {
            choiceModel.selectFiltered {
                it >= input.minimumAmountOfActivities
            }
        }
    }
}

class PrecedingSpawns( models: AllChoiceModels ) : SideTourActivityGenerator<SideTourPrecursorSet>(
    models.sideTourPrecursorActivityCountChoiceModel
)

class FollowingSpawns(models: AllChoiceModels) : SideTourActivityGenerator<SideTourSuccessorSet>(
     models.sideTourSuccessorActivityCountChoiceModel
)