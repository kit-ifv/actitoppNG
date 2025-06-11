package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.HTour
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiablePlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel

class GenerateSideToursFollowing(
    rngHelper: RNGHelper,
    choiceModel: ParametrizedDiscreteChoiceModel<Int, PreviousDaySituation, SuccessorTourAmountSet> = successorAmountChoiceModel,

    ) : DefaultSideTourDeterminer<SuccessorTourAmountSet>(rngHelper, choiceModel) {
//    override fun createChoiceSituation(
//        choice: Int,
//        day: DayWithBounds,
//        previousResult: Int?,
//        personWithRoutine: PersonWithRoutine,
//    ): PreviousDaySituation {
//        return PreviousDaySituation(
//            choice,
//            day.day,
//            plannedTourMap.getPreviousPlannedTourAmounts(day.day),
//            personWithRoutine,
//            plannedTourMap.getCurrentPlannedPrecursorTours(day.day),
//        )
//    }

//    fun generate(person: ActitoppPerson, routine: WeekRoutine, days: List<DayWithBounds>): List<Int> =
//        generate(PrecedingInput(PersonWithRoutine(person, routine),days ))

    override fun determineMinimumAmountOfTours(remainingNumberOfTours: Int): Int {
        return remainingNumberOfTours
    }

    override fun spawnTour(day: HDay): HTour {
        return day.generateFollowingTour()
    }

    override fun update(day: ModifiablePlannedTourAmounts, result: Int) {
        day.successorAmount = result
    }

}
