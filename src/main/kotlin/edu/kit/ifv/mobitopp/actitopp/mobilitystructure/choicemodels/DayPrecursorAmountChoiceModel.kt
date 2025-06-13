package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels

import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DefaultPrecursorTourParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.PrecursorTourAmountParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.PreviousDaySituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times

val precursorAmountChoiceModel =
    ModifiableDiscreteChoiceModel<Int, PreviousDaySituation, PrecursorTourAmountSet>(AllocatedLogit.create {
        option(0) { 0.0 }
        option(1, parameters = { one }, {
            val util = standardUtilityFunction(this, it)
            util
        })
        option(2, parameters = { two }, { standardUtilityFunction(this, it) })
        option(3, parameters = { three }, { standardUtilityFunction(this, it) })
        option(4, parameters = { four }, { standardUtilityFunction(this, it) })
        option(5, parameters = { five }, { standardUtilityFunction(this, it) })
    }).initializeWithParameters(DefaultPrecursorTourParameters)
private val standardUtilityFunction: PrecursorTourAmountParameters.(PreviousDaySituation) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFullTime +
            (it.dayMainActivityIsWork()) * mainActivityIsWork +
            (it.dayMainActivityIsEducation()) * mainActivityIsEducation +
            (it.dayMainActivityIsShopping()) * mainActivityIsShopping +
            (it.isSaturday()) * saturday +
            (it.isSunday()) * sunday +
            (it.isMale()) * male +
            (it.isAged10To17()) * age10To17 +
            (it.isAged26To35()) * aged26To35 +
            (it.isAged36To50()) * aged36To50 +
            (it.isAged51To60()) * aged51To60 +
            (it.commuteIn0To5km()) * commuteIn0To5km +
            (it.averageAmountOfToursIs1()) * averageAmountOfToursIs1 +
            (it.averageAmountOfToursIs2()) * averageAmountOfToursIs2 +
            (it.previousDayHasNoBeforeTour()) * previousDayHas0TourBeforeMainAct +
            (it.previousDayHasOneBeforeTour()) * previousDayHas1TourBeforeMainAct
}