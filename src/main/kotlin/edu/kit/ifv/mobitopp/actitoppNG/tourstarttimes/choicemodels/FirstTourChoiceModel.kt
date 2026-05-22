package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10M
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10M

import edu.kit.ifv.mobitopp.actitoppNG.utils.times


private val firstTourStartUtility: ParameterStep10M.(MainDurationAlternative) -> Double = {
    val durationOfActivities = it.dayPlan.durationOfActivities()
    val employmentCode = UtilityConverter.convertEmployment(it.person.employment)
    val tourActivityTypeCode = UtilityConverter.convertActivityType(it.tourPlan.mainActivity.activityType)
    base +
            when (employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 9, 10, 11 -> beruf_schueler
                4 -> beruf_azubi
                else -> .0
            } +
            when (tourActivityTypeCode) {
                5 -> tourtyp_work
                0 -> tourtyp_education
                3 -> tourtyp_shopping
                4 -> tourtyp_transport
                else -> .0
            } +
            (it.tag_sa()) * tag_sa +
            (it.tag_so()) * tag_so +
            when (durationOfActivities.inWholeHours) {
                in 4..<6 -> dauer_akt_tag_4bis6std
                in 6..<8 -> dauer_akt_tag_6bis8std
                in 8..<10 -> dauer_akt_tag_8bis10std
                in 10..<12 -> dauer_akt_tag_10bis12std
                in 12..<14 -> dauer_akt_tag_12bis14std

                else -> .0
            } +
            (it.taghat1akt()) * taghat1akt +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.taghat1tour()) * taghat1tour +
            (it.taghat2touren()) * taghat2touren +
            (it.haupttour_work()) * haupttour_work +
            (it.haupttour_education()) * haupttour_education +
            (it.touristhaupttour()) * touristhaupttour


}

context(params: PlanGenerationParameters)
val FIRST_TOUR_HISTOGRAM: ActivityDurationHistograms<ParameterCollectionStep10M>
    get() =
        params.firstTourHistogramParams.generateHistogram(
            ArrayHistogram.fromResource(
                identifier = Identifier.FIRST_TOUR_START_TIME
            ),
            firstTourStartUtility
        )