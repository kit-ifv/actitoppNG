package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DefaultSideTourMainActivityParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourMainActivityParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourMainActivitySet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.TourSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times

val tourMainActivityChoiceModel =
    ModifiableDiscreteChoiceModel<ActivityType, TourSituation, SideTourMainActivitySet>(AllocatedLogit.create {
        option(ActivityType.WORK, parameters = { work }, { standardUtilityFunction(this, it) })
        option(ActivityType.EDUCATION, parameters = { education }, { standardUtilityFunction(this, it) })
        option(ActivityType.LEISURE) { 0.0 }
        option(ActivityType.SHOPPING, parameters = { shopping }, { standardUtilityFunction(this, it) })
        option(ActivityType.TRANSPORT, parameters = { transport }, { standardUtilityFunction(this, it) })

    }).initializeWithParameters(DefaultSideTourMainActivityParameters)
private val standardUtilityFunction: SideTourMainActivityParameters.(TourSituation) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFullTime +
            (it.isParttimeEmployee()) * employmentPartTime +
            (it.isNotEarningMoney()) * employmentNotEarning +
            (it.isStudent()) * employmentStudent +
            (it.isVocational()) * employmentVocational +
            (it.dayMainActivityIsWork()) * mainTourWork +
            (it.dayMainActivityIsEducation()) * mainTourEducation +
            (it.dayMainActivityIsShopping()) * mainTourShopping +
            (it.dayMainActivityIsTransport()) * mainTourTransport +
            (it.isFriday()) * friday +
            (it.isSaturday()) * saturday +
            (it.isSunday()) * sunday +
            (it.isFirstTourOfDay()) * firstTourOfDay +
            (it.isSecondTourOfDay()) * secondTourOfDay +
            (it.isThirdTourOfDay()) * thirdTourOfDay +
            (it.isBeforeMainTour()) * beforeMainTour +
            (it.amountOfWorkingDaysIs0()) * noWorkDays +
            (it.amountOfEducationDaysIs0()) * noEducationDays +
            (it.amountOfShoppingDaysIs0()) * noShoppingDays +
            (it.amountOfServiceDaysIs0()) * noTransportDays +
            (it.amountOfWorkingDaysIs1()) * oneWorkDay +
            (it.amountOfEducationDaysIs1()) * oneEducationDay +
            (it.amountOfShoppingDaysIs1()) * oneShoppingDay +
            (it.amountOfServiceDaysIs1()) * oneTransportDay
}