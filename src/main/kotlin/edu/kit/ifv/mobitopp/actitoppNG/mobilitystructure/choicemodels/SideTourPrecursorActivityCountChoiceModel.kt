package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek

context(params: PlanGenerationParameters)
val step5AWithParams
    get() =
        DiscreteStructure<Int, TourAlternativeInt, SideTourPrecursorSet> {
            option(0) { 0.0 }
            loadOptionsMap(1..5) { _, it ->
                standardUtilityFunction(this, it)
            }
        }.multinomialLogit("Amount of precursor Activities in side tours")
            .build(params.step5AWithParamsParams)


private val standardUtilityFunction: SideTourPrecursorParameters.(TourAlternativeInt) -> Double = {
    base +

            (it.isBeforeMainTour()) * tourliegtvorhaupttour +
            (it.isAfterMainTour()) * tourliegtnachhaupttour +
            (it.tourMainActivityIsWork()) * tourtyp_work +
            (it.tourMainActivityIsEducation()) * tourtyp_education +
            (it.tourMainActivityIsShopping()) * tourtyp_shopping +
            (it.tourMainActivityIsTransport()) * tourtyp_transport +
            when (it.dayOfWeek) {
                DayOfWeek.SATURDAY -> tag_sa
                DayOfWeek.SUNDAY -> tag_so
                else -> 0.0
            } +
            when (it.ageCode) {
                AgeEncoder.isAged18To25, AgeEncoder.isAged26To35 -> alter_18bis35
                AgeEncoder.isAged36To50 -> alter_36bis50
                AgeEncoder.isAged51To60 -> alter_51bis60
                else -> 0.0
            } +
            when(it.amountOfTours()) {
                1 -> taghat1tour
                2 -> taghat2touren
                else -> 0.0
            } +
            (it.averageAmountOfActivitiesIs1()) * mean_1akt +
            (it.averageAmountOfActivitiesIs2()) * mean_2akt +
            (it.averageAmountOfActivitiesIs3()) * mean_3akt
}
