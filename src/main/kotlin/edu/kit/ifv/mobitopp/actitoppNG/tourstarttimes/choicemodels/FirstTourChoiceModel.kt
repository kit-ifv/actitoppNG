package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.performance.TimeBlockEncoder
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10M
import edu.kit.ifv.mobitopp.actitoppNG.utils.times

data class FirstTourStartCharacteristic(
    val durationOfActivities: Int,
    val employmentCode: Int,
    val tourActivity: ActivityType,
    val tourMainActivity: ActivityType,
    val dayCode: Int,
    val amountActivitiesToday: Int,
    val amountToursToday: Int,
    val tourIsMainTour: Boolean,
) {
    constructor(alternative: MainDurationAlternative) : this(
        durationOfActivities = TimeBlockEncoder.convert(alternative.dayPlan.durationOfActivities()),
        employmentCode = UtilityConverter.convertEmployment(alternative.person.employment),
        tourActivity= alternative.tourPlan.mainActivity.activityType,
        tourMainActivity = alternative.dayPlan.mainActivityType(),
        dayCode = UtilityConverter.convertWeekDay(alternative.dayPlan.durationDay.weekday),
        amountActivitiesToday = alternative.dayPlanAmountOfActivities,
        amountToursToday = alternative.tag_anzahl_touren,
        tourIsMainTour = alternative.touristhaupttour()
    )
}

private val firstTourStartUtility: ParameterStep10M.(FirstTourStartCharacteristic) -> Double = {
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
            when(it.dayCode) {
                6 -> tag_sa
                7 -> tag_so
                else -> 0.0
            } +
            when (it.durationOfActivities) {

                2 -> dauer_akt_tag_4bis6std
                3 -> dauer_akt_tag_6bis8std
                4 -> dauer_akt_tag_8bis10std
                5 -> dauer_akt_tag_10bis12std
                6 -> dauer_akt_tag_12bis14std

                else -> .0
            } +
            when(it.amountActivitiesToday) {
                1 -> taghat1akt
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            when(it.amountToursToday) {
                1 -> taghat1tour
                2 -> taghat2touren
                else -> 0.0
            } +
            when(it.tourMainActivity) {
                ActivityType.WORK -> haupttour_work
                ActivityType.EDUCATION -> haupttour_education
                else -> 0.0
            } +
            (it.tourIsMainTour) * touristhaupttour
}

context(params: PlanGenerationParameters)
val FIRST_TOUR_HISTOGRAM: ActivityDurationHistograms
    get() =
        params.firstTourHistogramParams.generateHistogram(
            ArrayHistogram.fromResource(
                identifier = Identifier.FIRST_TOUR_START_TIME
            ),
            { FirstTourStartCharacteristic(it) },
            firstTourStartUtility
        )