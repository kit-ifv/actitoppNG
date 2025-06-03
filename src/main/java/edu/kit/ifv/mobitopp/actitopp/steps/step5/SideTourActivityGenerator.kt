package edu.kit.ifv.mobitopp.actitopp.steps.step5

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.steps.step3.TourSituationInt
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel

abstract class SideTourActivityGenerator<P>(
    val rngHelper: RNGKeeper,
    val choiceModel: ParametrizedDiscreteChoiceModel<Int, TourSituationInt, P>,
) : GenerateSideTourActivities {
    override fun generateActivityAmount(input: SideTourActivityInput): Int {

        val options = choiceModel.registeredOptions().toMutableSet()
        options.removeIf { it < input.minimumAmountOfActivities }

        val converter: (Int) -> TourSituationInt = {
            TourSituationInt(it, input.person, input.routine, input.currentDay, input.tour, input.amountOfActivitiesBeforeMainAct)
        }
        val rnd = rngHelper.pull(stringID)
        return choiceModel.select(options, rnd, converter)
    }

    abstract val stringID: String


}

class PrecedingSpawns(rngHelper: RNGKeeper) : SideTourActivityGenerator<ParameterCollectionStep5A>(
    rngHelper, step5AWithParams,
) {
    override val stringID: String = "5A"
}

class FollowingSpawns(rngHelper: RNGKeeper) : SideTourActivityGenerator<ParameterCollectionStep5B>(
    rngHelper, step5BWithParams,
) {
    override val stringID: String = "5B"
}