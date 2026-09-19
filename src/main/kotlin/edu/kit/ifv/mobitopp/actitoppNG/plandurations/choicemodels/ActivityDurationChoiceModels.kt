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
import edu.kit.ifv.mobitopp.actitoppNG.utils.Position
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import java.time.DayOfWeek

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
            (it.original.dayOfWeek == DayOfWeek.SUNDAY) * tag_so +
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

data class UtilityFunction8DParams(
    val averageTimeOfActivityCode: Int,
    val dayCode: Int,
    val employmentCode: Int,
    val anzaktwieanztagemitzweck: Int,
    val activityType: ActivityType,
    val timeBudgetCategory: Int,
    val activitiesInTour: Int,
    val activitiesInDayplan: Int,
    val isLastTourOfDay: Int,
    val numToursToday: Int,
    val tourRelativeToMainTour: Position,
) {
    constructor(alternative: MainDurationAlternative) : this(
        averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(alternative.dayPlan
            .getBudget(alternative.activityType)),
        dayCode = UtilityConverter.convertWeekDay(alternative.dayPlan.durationDay.weekday),
        employmentCode = UtilityConverter.convertEmployment(alternative.person.employment),
        anzaktwieanztagemitzweck = alternative.anzaktwieanztagemitzweck().I,
        activityType = alternative.activityType,
        timeBudgetCategory = alternative.planTimeBudgets.getCategory(alternative.activityType).toInt(),
        activitiesInTour = alternative.tourPlan.size,
        activitiesInDayplan = alternative.dayPlanAmountOfActivities,
        isLastTourOfDay = alternative.isLastTourOfDay.I,
        numToursToday = alternative.tag_anzahl_touren,
        tourRelativeToMainTour = alternative.tourPlan.position
    )
}

fun ParameterStep8D.standardUtilityFunction8D(it: UtilityFunction8DParams): Double {
     return base +
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
            when (it.timeBudgetCategory) {
                1 -> wochenzbudget_zweck_kat1
                2 -> wochenzbudget_zweck_kat2
                3 -> wochenzbudget_zweck_kat3
                4 -> wochenzbudget_zweck_kat4
                5 -> wochenzbudget_zweck_kat5
                else -> .0
            } +
            when (it.activitiesInTour) {
                1 -> tourhat1akt
                2 -> tourhat2akt
                3 -> tourhat3akt
                else -> .0
            } +
            when(it.activitiesInDayplan) {
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            (it.isLastTourOfDay) * letztetourdestages +
            when(it.numToursToday) {
                2 -> taghat2touren
                else -> 0.0
            } +
            when (it.employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 4, 9, 10, 11 -> beruf_schueler_azubi
                else -> .0
            } +
             when(it.tourRelativeToMainTour) {
                 Position.BEFORE -> tourliegtvorhaupttour
                 Position.AFTER -> tourliegtnachhaupttour
                 else -> .0
             }
}

data class StandardUtilityFunction8BParams(
    val averageTimeOfActivityCode: Int,
    val dayCode: Int,
    val employmentCode: Int,
    val anzaktwieanztagemitzweck: Int,
    val activity: ActivityType,
    val wochenzbudget_zweck_kategorie: Int,
    val numActivitiesInTour: Int,
    val numActivitiesInDayplan: Int,
    val numToursToday: Int,
    val positionToMainTour: Position,
) {
    constructor(alternative: MainDurationAlternative): this(
        averageTimeOfActivityCode = UtilityConverter.convertToOneHourBlock(alternative.dayPlan.getBudget(alternative.activityType)),
        dayCode = UtilityConverter.convertWeekDay(alternative.dayPlan.durationDay.weekday),
        employmentCode = UtilityConverter.convertEmployment(alternative.person.employment),
        anzaktwieanztagemitzweck= alternative.anzaktwieanztagemitzweck().I,
        activity = alternative.activityType,
        wochenzbudget_zweck_kategorie= alternative.planTimeBudgets.getCategory(alternative.activityType).toInt(),
        numActivitiesInTour= alternative.tourPlan.size,
        numActivitiesInDayplan= alternative.dayPlan.amountOfActivities,
        numToursToday=  alternative.tag_anzahl_touren,
        positionToMainTour= alternative.tourPlan.position,
    )
}

private fun ParameterStep8B.standardUtilityFunction8B(it: StandardUtilityFunction8BParams): Double {
    return base +
            when (it.averageTimeOfActivityCode) {
                1 -> mittl_zeit_akt_60bis119min
                2 -> mittl_zeit_akt_120bis179min
                3 -> mittl_zeit_akt_180bis239min
                4 -> mittl_zeit_akt_240bis299min
                5 -> mittl_zeit_akt_300bis359min
                6 -> mittl_zeit_akt_360bis419min
                7 -> mittl_zeit_akt_420bis479min
                else -> .0
            } +
            when (it.dayCode) {
                5 -> tag_fr
                6 -> tag_sa
                7 -> tag_so
                else -> .0
            } +
            (it.anzaktwieanztagemitzweck) * anzaktwieanztagemitzweck +
            when (it.activity) {
                ActivityType.WORK -> aktzweck_work
                ActivityType.EDUCATION -> aktzweck_education
                ActivityType.SHOPPING -> aktzweck_shopping
                ActivityType.TRANSPORT -> aktzweck_transport
                else -> .0
            } +
            when (it.wochenzbudget_zweck_kategorie) {
                1 -> wochenzbudget_zweck_kat1
                2 -> wochenzbudget_zweck_kat2
                3 -> wochenzbudget_zweck_kat3
                4 -> wochenzbudget_zweck_kat4
                5 -> wochenzbudget_zweck_kat5
                else -> .0
            } +
            when (it.numActivitiesInTour) {
                1 -> tourhat1akt
                2 -> tourhat2akt
                3 -> tourhat3akt
                else -> .0

            } +
            when (it.numActivitiesInDayplan) {
                1 -> taghat1akt
                2 -> taghat2akt
                3 -> taghat3akt
                else -> 0.0
            } +
            when(it.numToursToday) {
                1 -> taghat1tour
                2 -> taghat2touren
                else -> 0.0
            } +
            when (it.employmentCode) {
                0 -> beruf_vollzeit
                1, 7, 8 -> beruf_teilzeit
                3, 4, 9, 10, 11 -> beruf_schueler_azubi
                else -> .0
            } +
            when(it.positionToMainTour) {
                Position.BEFORE -> tourliegtvorhaupttour
                Position.AFTER -> tourliegtnachhaupttour
                else -> .0
            }
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
        { UtilityFunction8DParams(it) },
        { params -> standardUtilityFunction8D(params) },
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
            SingularUtilityStructureBuilder(
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
            }.build("MAJOR_NEW")

        return ActivityDurationHistograms(histograms, build)
    }

context(params: PlanGenerationParameters)
val LEAD
    get() = params.leadActivityDurationParams.generateHistogram(
        ArrayHistogram.fromResource(
            identifier = Identifier.LEAD_ACTIVITY_DURATION
        ),
        { StandardUtilityFunction8BParams(it) },
        {params -> standardUtilityFunction8B(params) },
    )