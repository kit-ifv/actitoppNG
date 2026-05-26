package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8J
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.utils.times


private val minorFunction: ParameterStep8J.(MainDurationAlternative) -> Double = {

//    val durationMainAct = it.dayPlan.durationOfMainActivities
    val durationMainActCode = UtilityConverter.convertToTwoHourBlock(it.dayPlan.durationOfMainActivities)
        .coerceAtMost(7)
    val averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(it.dayPlan.getBudget(it.activityType))
    val numberOfActivities = it.dayPlan.amountOfActivities
    val activityTypeCode = UtilityConverter.convertActivityType(it.activityType)
    val tourMainActivityTypeCode = UtilityConverter.convertActivityType(it.tourPlan.mainActivity.activityType)
    base +
            when (durationMainActCode) {
                2 -> dauer_hauptakt_tag_4bis6std
                3 -> dauer_hauptakt_tag_6bis8std
                4 -> dauer_hauptakt_tag_8bis10std
                5 -> dauer_hauptakt_tag_10bis12std
                6 -> dauer_hauptakt_tag_12bis14std
                7 -> dauer_hauptakt_tag_ueber14std
                else -> .0
            } +
            when (averageTimeOfActivityCode) {
                0 -> mittl_zeit_akt_1bis14min
                1 -> mittl_zeit_akt_15bis29min
                2, 3 -> mittl_zeit_akt_30bis59min
                4, 5, 6, 7 -> mittl_zeit_akt_60bis119min
                else -> .0

            } +
            (it.tag_so()) * tag_so +
            when (activityTypeCode) {
                5 -> aktzweck_work
                0 -> aktzweck_education
                3 -> aktzweck_shopping
                4 -> aktzweck_transport
                else -> 0.0
            } +
            when (numberOfActivities) {
                2 -> taghat2akt
                3 -> taghat3akt
                4 -> taghat4akt
                5 -> taghat5akt
                6 -> taghat6akt
                else -> .0
            } +
            when (tourMainActivityTypeCode) {
                5 -> tourtyp_work
                0 -> tourtyp_education
                3 -> tourtyp_shopping
                4 -> tourtyp_transport
                else -> .0
            } +
            (it.tourhat2akt()) * tourhat2akt +
            (it.taghat1tour()) * taghat1tour +
            (it.taghat2touren()) * taghat2touren +
            (it.aktliegtvorhauptakt()) * aktliegtvorhauptakt +
            (it.tourliegtvorhaupttour()) * tourliegtvorhaupttour +
            (it.tourliegtnachhaupttour()) * tourliegtnachhaupttour

}
private val standardUtilityFunction8D: ParameterStep8D.(MainDurationAlternative) -> Double = {
    val averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(it.dayPlan.getBudget(it.activityType))
    val dayCode = UtilityConverter.convertWeekDay(it.dayPlan.durationDay.weekday)
    val activityCode = UtilityConverter.convertActivityType(it.activityType)
    val employmentCode = UtilityConverter.convertEmployment(it.person.employment)
    base +

            when (averageTimeOfActivityCode) {

                0 -> mittl_zeit_akt_1bis14min
                1 -> mittl_zeit_akt_15bis29min
                2, 3 -> mittl_zeit_akt_30bis59min
                4, 5, 6, 7 -> mittl_zeit_akt_60bis119min
                8, 9, 10, 11 -> mittl_zeit_akt_120bis179min
                12, 13, 14, 15 -> mittl_zeit_akt_180bis239min
                else -> .0
            } +

            when (dayCode) {
                5 -> tag_fr
                6 -> tag_sa
                7 -> tag_so
                else -> .0
            } +
            (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
            when (activityCode) {
                5 -> aktzweck_work
                0 -> aktzweck_education
                3 -> aktzweck_shopping
                4 -> aktzweck_transport
                else -> .0
            } +
            when (it.planTimeBudgets.getCategory(it.activityType).toInt()) {
                1 -> wochenzbudget_zweck_kat1
                2 -> wochenzbudget_zweck_kat2
                3 -> wochenzbudget_zweck_kat3
                4 -> wochenzbudget_zweck_kat4
                5 -> wochenzbudget_zweck_kat5
                else -> .0
            } +
            when (it.tourPlan.size) {
                1 -> tourhat1akt
                2 -> tourhat2akt
                3 -> tourhat3akt
                else -> .0
            } +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.letztetourdestages()) * letztetourdestages +
            (it.taghat2touren()) * taghat2touren +
            when (employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 4, 9, 10, 11 -> beruf_schueler_azubi
                else -> .0
            } +
            (it.tourliegtvorhaupttour()) * tourliegtvorhaupttour +
            (it.tourliegtnachhaupttour()) * tourliegtnachhaupttour

}
private val standardUtilityFunction8B: ParameterStep8B.(MainDurationAlternative) -> Double = {
    val averageTimeOfActivityCode = UtilityConverter.convertToOneHourBlock(it.dayPlan.getBudget(it.activityType))
    val dayCode = UtilityConverter.convertWeekDay(it.dayPlan.durationDay.weekday)
    val activityCode = UtilityConverter.convertActivityType(it.activityType)
    val employmentCode = UtilityConverter.convertEmployment(it.person.employment)
    base +
            when (averageTimeOfActivityCode) {
                1 -> mittl_zeit_akt_60bis119min
                2 -> mittl_zeit_akt_120bis179min
                3 -> mittl_zeit_akt_180bis239min
                4 -> mittl_zeit_akt_240bis299min
                5 -> mittl_zeit_akt_300bis359min
                6 -> mittl_zeit_akt_360bis419min
                7 -> mittl_zeit_akt_420bis479min
                else -> .0
            } +
            when (dayCode) {
                5 -> tag_fr
                6 -> tag_sa
                7 -> tag_so
                else -> .0
            } +
            (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
            when (activityCode) {
                5 -> aktzweck_work
                0 -> aktzweck_education
                3 -> aktzweck_shopping
                4 -> aktzweck_transport
                else -> .0
            } +
            when (it.planTimeBudgets.getCategory(it.activityType).toInt()) {
                1 -> wochenzbudget_zweck_kat1
                2 -> wochenzbudget_zweck_kat2
                3 -> wochenzbudget_zweck_kat3
                4 -> wochenzbudget_zweck_kat4
                5 -> wochenzbudget_zweck_kat5
                else -> .0
            } +
            when (it.tourPlan.size) {
                1 -> tourhat1akt
                2 -> tourhat2akt
                3 -> tourhat3akt
                else -> .0

            } +

            when (it.dayPlan.amountOfActivities) {
                1 -> taghat1akt
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            (it.taghat1tour()) * taghat1tour +
            (it.taghat2touren()) * taghat2touren +
            when (employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 4, 9, 10, 11 -> beruf_schueler_azubi
                else -> .0
            } +
            (it.tourliegtvorhaupttour()) * tourliegtvorhaupttour +
            (it.tourliegtnachhaupttour()) * tourliegtnachhaupttour
}

context(params: PlanGenerationParameters)
val MINOR
    get() = params.minorActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.MINOR_ACTIVITY_DURATION
        ),
        minorFunction,
    )

context(params: PlanGenerationParameters)
val MAJOR
    get() = params.majorActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.MAJOR_ACTIVITY_DURATION
        ),
        standardUtilityFunction8D,
    )

context(params: PlanGenerationParameters)
val LEAD
    get() = params.leadActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.LEAD_ACTIVITY_DURATION
        ),
        standardUtilityFunction8B,
    )