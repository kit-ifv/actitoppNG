package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isNotEarning
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek

context(params: PlanGenerationParameters)
val tourMainActivityChoiceModel
    get() =
        DiscreteStructure<ActivityType, TourAlternative, SideTourMainActivitySet> {
            loadOptionsMap(listOf(ActivityType.WORK, ActivityType.EDUCATION)) { _, it ->
                standardUtilityFunction(this, it)
            }
            option(ActivityType.LEISURE) { 0.0 }
            loadOptionsMap(listOf(ActivityType.SHOPPING, ActivityType.TRANSPORT)) { _, it ->
                standardUtilityFunction(this, it)
            }
        }.multinomialLogit("Main Activity of the tours that are not the main tour.")
            .build(params.tourMainActivityChoiceModelParams)

private val standardUtilityFunction: SideTourMainActivityParameters.(TourAlternative) -> Double = {
    base +
            when (it.employment) {
                Employment.FULLTIME -> employmentFullTime
                in isParttime -> employmentPartTime
                in isNotEarning -> employmentNotEarning
                in isStudent -> employmentStudent
                Employment.VOCATIONAL -> employmentVocational
                else -> 0.0
            } +
            when (it.dayMainActivity) {
                ActivityType.WORK -> mainTourWork
                ActivityType.EDUCATION -> mainTourEducation
                ActivityType.SHOPPING -> mainTourShopping
                ActivityType.TRANSPORT -> mainTourTransport
                else -> 0.0
            } +
            when (it.dayOfWeek) {
                DayOfWeek.FRIDAY -> friday
                DayOfWeek.SATURDAY -> saturday
                DayOfWeek.SUNDAY -> sunday
                else -> 0.0
            } +
            when(it.tourIndexOfDay) {
                0 -> firstTourOfDay
                1 -> secondTourOfDay
                2 -> thirdTourOfDay
                else -> 0.0
            } +
            (it.isBeforeMainTour) * beforeMainTour +
            when(it.amountOfWorkingDays) {
                0 -> noWorkDays
                1 -> oneWorkDay
                else -> 0.0
            } +
            when(it.amountOfEducationDays) {
                0 -> noEducationDays
                1 -> oneEducationDay
                else -> 0.0
            } +
            when(it.amountOfShoppingDays) {
                0 -> noShoppingDays
                1 -> oneShoppingDay
                else -> 0.0
            } +
            when(it.amountOfServiceDays) {
                0 -> noTransportDays
                1 -> oneTransportDay
                else -> 0.0
            }
}