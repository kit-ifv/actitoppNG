package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternativeEncoded
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10M
import edu.kit.ifv.mobitopp.actitoppNG.utils.times


private val firstTourStartUtility: ParameterStep10M.(MainDurationAlternativeEncoded) -> Double = {
    base +
            when (it.employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 9, 10, 11 -> beruf_schueler
                4 -> beruf_azubi
                else -> .0
            } +
            when (it.tourActivity) {
                ActivityType.WORK -> tourtyp_work
                ActivityType.EDUCATION -> tourtyp_education
                ActivityType.SHOPPING -> tourtyp_shopping
                ActivityType.TRANSPORT -> tourtyp_transport
                else -> .0
            } +
            (it.original.tag_sa()) * tag_sa +
            (it.original.tag_so()) * tag_so +
            when (it.durationOfActivities) {

                2 -> dauer_akt_tag_4bis6std
                3 -> dauer_akt_tag_6bis8std
                4 -> dauer_akt_tag_8bis10std
                5 -> dauer_akt_tag_10bis12std
                6 -> dauer_akt_tag_12bis14std

                else -> .0
            } +
            when(it.original.dayPlanAmountOfActivities) {
                1 -> taghat1akt
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            (it.original.taghat1tour()) * taghat1tour +
            (it.original.taghat2touren()) * taghat2touren +
            (it.original.haupttour_work()) * haupttour_work +
            (it.original.haupttour_education()) * haupttour_education +
            (it.original.touristhaupttour()) * touristhaupttour


}

context(params: PlanGenerationParameters)
val FIRST_TOUR_HISTOGRAM: ActivityDurationHistograms
    get() =
        params.firstTourHistogramParams.generateHistogram(
            ArrayHistogram.fromResource(
                identifier = Identifier.FIRST_TOUR_START_TIME
            ),
            { MainDurationAlternativeEncoded(it) },
            firstTourStartUtility
        )