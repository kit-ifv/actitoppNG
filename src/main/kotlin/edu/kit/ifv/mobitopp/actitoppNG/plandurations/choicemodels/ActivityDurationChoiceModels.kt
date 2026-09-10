package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels

import edu.kit.ifv.discretechoice.extensions.SingularUtilityStructureBuilder
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.performance.TimeBlockEncoder
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8J
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8D
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.utils.I
import edu.kit.ifv.mobitopp.actitoppNG.utils.times

data class MinorFunctionEncodedCharacteristic(
    val durationMainActCode: Int,
    val averageTimeOfActivityCode: Int,
    val numberOfActivities: Int,
    val original: MainDurationAlternative
) {
    constructor(alternative: MainDurationAlternative) : this(
        durationMainActCode= TimeBlockEncoder.convert(alternative.dayPlan.durationOfMainActivities)
            .coerceAtMost(7),
        averageTimeOfActivityCode= UtilityConverter.convertToFifteenMinuteBlock(alternative.dayPlan.getBudget(alternative.activityType)),
        numberOfActivities= alternative.dayPlan.amountOfActivities,
        original= alternative
    )
}

private val minorFunction: ParameterStep8J.(MinorFunctionEncodedCharacteristic) -> Double = {
    base +
            when (it.durationMainActCode) {
                2 -> dauer_hauptakt_tag_4bis6std
                3 -> dauer_hauptakt_tag_6bis8std
                4 -> dauer_hauptakt_tag_8bis10std
                5 -> dauer_hauptakt_tag_10bis12std
                6 -> dauer_hauptakt_tag_12bis14std
                7 -> dauer_hauptakt_tag_ueber14std
                else -> .0
            } +
            when (it.averageTimeOfActivityCode) {
                0 -> mittl_zeit_akt_1bis14min
                1 -> mittl_zeit_akt_15bis29min
                2, 3 -> mittl_zeit_akt_30bis59min
                4, 5, 6, 7 -> mittl_zeit_akt_60bis119min
                else -> .0

            } +
            (it.original.tag_so()) * tag_so +
            when (it.original.activityType) {
                ActivityType.WORK -> aktzweck_work
                ActivityType.EDUCATION -> aktzweck_education
                ActivityType.SHOPPING -> aktzweck_shopping
                ActivityType.TRANSPORT -> aktzweck_transport
                else -> 0.0
            } +
            when (it.numberOfActivities) {
                2 -> taghat2akt
                3 -> taghat3akt
                4 -> taghat4akt
                5 -> taghat5akt
                6 -> taghat6akt
                else -> .0
            } +
            when (it.original.tourPlan.mainActivity.activityType) {
              ActivityType.WORK -> tourtyp_work
              ActivityType.EDUCATION -> tourtyp_education
              ActivityType.SHOPPING -> tourtyp_shopping
              ActivityType.TRANSPORT -> tourtyp_transport
                else -> .0
            } +
            (it.original.tourhat2akt()) * tourhat2akt +
            when(it.original.tag_anzahl_touren) {
                1 -> taghat1tour
                2 -> taghat2touren
                else -> 0.0
            } +
            (it.original.aktliegtvorhauptakt()) * aktliegtvorhauptakt +
            (it.original.tourliegtvorhaupttour()) * tourliegtvorhaupttour +
            (it.original.tourliegtnachhaupttour()) * tourliegtnachhaupttour

}

private val standardUtilityFunction8D: ParameterStep8D.(MainDurationAlternative) -> Double = {
    val averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(it.dayPlan.getBudget(it.activityType))
    val dayCode = UtilityConverter.convertWeekDay(it.dayPlan.durationDay.weekday)
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
            when (it.activityType) {
                ActivityType.WORK -> aktzweck_work
                ActivityType.EDUCATION -> aktzweck_education
                ActivityType.SHOPPING -> aktzweck_shopping
                ActivityType.TRANSPORT -> aktzweck_transport
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
            when(it.dayPlanAmountOfActivities) {
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            (it.letztetourdestages()) * letztetourdestages +
            when(it.tag_anzahl_touren) {
                2 -> taghat2touren
                else -> 0.0
            } +
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
            when (it.activityType) {
                ActivityType.WORK -> aktzweck_work
                ActivityType.EDUCATION -> aktzweck_education
                ActivityType.SHOPPING -> aktzweck_shopping
                ActivityType.TRANSPORT -> aktzweck_transport
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
            when(it.tag_anzahl_touren) {
                1 -> taghat1tour
                2 -> taghat2touren
                else -> 0.0
            } +
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
        { MinorFunctionEncodedCharacteristic(it) },
        minorFunction,
    )

context(params: PlanGenerationParameters)
val MAJOR
    get() = params.majorActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.MAJOR_ACTIVITY_DURATION
        ),
        { it },
        standardUtilityFunction8D,
    )

context(params: PlanGenerationParameters)
val COMPARE_MAJOR_VERSION get() = SelectorWithMemory(MAJOR, MAJOR_NEW)

private data class DurationAlternativeEncoded(
    val averageTimeOfActivityCode: Int,
    val dayCode: Int,
    val activityType: ActivityType,
    val employmentCode: Int,
    val anzaktwieanztagemitzweck: Int,
    val planTimeBudgetsCode: Int,
    val tourPlanSize: Int,
    val tagAmountAlternatives: Int,
    val lastTourOfDayCode: Int,
    val dayHasTwoToursCode: Int,
    val tourPositionCode: Int,
) {
    companion object {
        fun fromAlternative(it: MainDurationAlternative): DurationAlternativeEncoded {
            return DurationAlternativeEncoded(
                averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(
                    it.dayPlan.getBudget(
                        it
                            .activityType
                    )
                ),
                dayCode = UtilityConverter.convertWeekDay(it.dayPlan.durationDay.weekday),
                activityType = it.activityType,
                employmentCode = UtilityConverter.convertEmployment(it.person.employment),
                anzaktwieanztagemitzweck = it.anzaktwieanztagemitzweck().I,
                planTimeBudgetsCode = it.planTimeBudgets.getCategory(it.activityType).toInt(),
                tourPlanSize = it.tourPlan.size,
                tagAmountAlternatives = it.dayPlan.amountOfActivities,
                lastTourOfDayCode = it.letztetourdestages().I,
                dayHasTwoToursCode = (it.tag_anzahl_touren == 2).I,
                tourPositionCode = it.tourPlan.position.ordinal,
            )
        }
    }
}



context(params: PlanGenerationParameters)
val MAJOR_NEW: ActivityDurationHistograms
    get() {
        val histograms = ArrayHistogram.fromResource(
            identifier = Identifier.MAJOR_ACTIVITY_DURATION
        )
        val build =
            SingularUtilityStructureBuilder<ArrayHistogram, MainDurationAlternative, ParameterStep8D, DurationAlternativeEncoded>(
                histograms.toSet(),
                converter = DurationAlternativeEncoded::fromAlternative,
                parameters = ParametersStep8D.toTypedArray()
            ) { a, it, p ->
                p.run {
                    base +

                            when (it.averageTimeOfActivityCode) {

                                0 -> mittl_zeit_akt_1bis14min
                                1 -> mittl_zeit_akt_15bis29min
                                2, 3 -> mittl_zeit_akt_30bis59min
                                4, 5, 6, 7 -> mittl_zeit_akt_60bis119min
                                8, 9, 10, 11 -> mittl_zeit_akt_120bis179min
                                12, 13, 14, 15 -> mittl_zeit_akt_180bis239min
                                else -> .0
                            } +

                            when (it.dayCode) {
                                5 -> tag_fr
                                6 -> tag_sa
                                7 -> tag_so
                                else -> .0
                            } +
                            (it.anzaktwieanztagemitzweck) * anzaktwieanztagemitzweck +
                            when (it.activityType) {
                                ActivityType.WORK -> aktzweck_work
                                ActivityType.EDUCATION -> aktzweck_education
                                ActivityType.SHOPPING -> aktzweck_shopping
                                ActivityType.TRANSPORT -> aktzweck_transport
                                else -> .0
                            } +
                            when (it.planTimeBudgetsCode) {
                                1 -> wochenzbudget_zweck_kat1
                                2 -> wochenzbudget_zweck_kat2
                                3 -> wochenzbudget_zweck_kat3
                                4 -> wochenzbudget_zweck_kat4
                                5 -> wochenzbudget_zweck_kat5
                                else -> .0
                            } +
                            when (it.tourPlanSize) {
                                1 -> tourhat1akt
                                2 -> tourhat2akt
                                3 -> tourhat3akt
                                else -> .0
                            } +

                            when (it.tagAmountAlternatives) {
                                2 -> taghat2akt
                                3 -> taghat3akt
                                else -> 0.0
                            } +
                            (it.lastTourOfDayCode) * letztetourdestages +
                            (it.dayHasTwoToursCode) * taghat2touren +
                            when (it.employmentCode) {
                                0 -> beruf_vollzeit
                                1, 7, 8 -> beruf_teilzeit
                                3, 4, 9, 10, 11 -> beruf_schueler_azubi
                                else -> .0
                            } +
                            when (it.tourPositionCode) {
                                0 -> tourliegtvorhaupttour
                                2 -> tourliegtnachhaupttour
                                else -> .0
                            }
                }


            }.build()

        return ActivityDurationHistograms(histograms, build)
    }

context(params: PlanGenerationParameters)
val LEAD
    get() = params.leadActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.LEAD_ACTIVITY_DURATION
        ),
        { it },
        standardUtilityFunction8B,
    )