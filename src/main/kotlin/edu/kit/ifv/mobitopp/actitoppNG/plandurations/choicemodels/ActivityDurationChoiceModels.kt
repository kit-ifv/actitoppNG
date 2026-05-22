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
import kotlin.time.Duration.Companion.minutes


private val minorFunction: ParameterStep8J.(MainDurationAlternative) -> Double = {

    val durationMainAct = it.dayPlan.durationOfMainActivities
    val averageTimeOfActivity = it.dayPlan.getBudget(it.activityType)
    val numberOfActivities = it.dayPlan.amountOfActivities
    val activityTypeCode = UtilityConverter.convertActivityType(it.activityType)
    val tourMainActivityTypeCode = UtilityConverter.convertActivityType(it.tourPlan.mainActivity.activityType)
    base +
            when (durationMainAct.inWholeHours) {
                in 4..<6 -> dauer_hauptakt_tag_4bis6std
                in 6..<8 -> dauer_hauptakt_tag_6bis8std
                in 8..<10 -> dauer_hauptakt_tag_8bis10std
                in 10..<12 -> dauer_hauptakt_tag_10bis12std
                in 12..<14 -> dauer_hauptakt_tag_12bis14std
                in 14..<Int.MAX_VALUE -> dauer_hauptakt_tag_ueber14std
                else -> .0
            } +
            when (averageTimeOfActivity.inWholeMinutes) {
                in 1..<15 -> mittl_zeit_akt_1bis14min
                in 15..<30 -> mittl_zeit_akt_15bis29min
                in 30..<60 -> mittl_zeit_akt_30bis59min
                in 60..<120 -> mittl_zeit_akt_60bis119min
                else -> .0
            } +
            (it.tag_so()) * tag_so +
            when(activityTypeCode) {
                5 -> aktzweck_work
                0 -> aktzweck_education
                3  -> aktzweck_shopping
                4 -> aktzweck_transport
                else -> 0.0
            } +
            when(numberOfActivities) {
                2 -> taghat2akt
                3 -> taghat3akt
                4 -> taghat4akt
                5 -> taghat5akt
                6 -> taghat6akt
                else -> .0
            } +
            when(tourMainActivityTypeCode) {
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
    val averageTimeOfActivity = it.dayPlan.getBudget(it.activityType)
    base +

            when (averageTimeOfActivity.inWholeMinutes) {
                in 1..<15 -> mittl_zeit_akt_1bis14min
                in 15..<30 -> mittl_zeit_akt_15bis29min
                in 30..<60 -> mittl_zeit_akt_30bis59min
                in 60..<120 -> mittl_zeit_akt_60bis119min
                in 120..<180 -> mittl_zeit_akt_120bis179min
                in 180..<240 -> mittl_zeit_akt_180bis239min
                else -> .0
            } +
            (it.tag_fr()) * tag_fr +
            (it.tag_sa()) * tag_sa +
            (it.tag_so()) * tag_so +
            (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
            (it.aktzweck_work()) * aktzweck_work +
            (it.aktzweck_education()) * aktzweck_education +
            (it.aktzweck_shopping()) * aktzweck_shopping +
            (it.aktzweck_transport()) * aktzweck_transport +
            (it.wochenzbudget_zweck_kat1()) * wochenzbudget_zweck_kat1 +
            (it.wochenzbudget_zweck_kat2()) * wochenzbudget_zweck_kat2 +
            (it.wochenzbudget_zweck_kat3()) * wochenzbudget_zweck_kat3 +
            (it.wochenzbudget_zweck_kat4()) * wochenzbudget_zweck_kat4 +
            (it.wochenzbudget_zweck_kat5()) * wochenzbudget_zweck_kat5 +
            (it.tourhat1akt()) * tourhat1akt +
            (it.tourhat2akt()) * tourhat2akt +
            (it.tourhat3akt()) * tourhat3akt +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.letztetourdestages()) * letztetourdestages +
            (it.taghat2touren()) * taghat2touren +
            (it.beruf_vollzeit()) * beruf_vollzeit +
            (it.beruf_teilzeit()) * beruf_teilzeit +
            (it.beruf_schueler_azubi()) * beruf_schueler_azubi +
            (it.tourliegtvorhaupttour()) * tourliegtvorhaupttour +
            (it.tourliegtnachhaupttour()) * tourliegtnachhaupttour

}
private val standardUtilityFunction8B: ParameterStep8B.(MainDurationAlternative) -> Double = {
    val averageTimeOfActivity = it.dayPlan.getBudget(it.activityType)
    base +
            when (averageTimeOfActivity.inWholeMinutes) {
                in 60..<120 -> mittl_zeit_akt_60bis119min
                in 120..<180 -> mittl_zeit_akt_120bis179min
                in 180..<240 -> mittl_zeit_akt_180bis239min
                in 240..<300 -> mittl_zeit_akt_240bis299min
                in 300..<360 -> mittl_zeit_akt_300bis359min
                in 360..<420 -> mittl_zeit_akt_360bis419min
                in 420..<480 -> mittl_zeit_akt_420bis479min
                else -> .0
            } +
            (it.tag_fr()) * tag_fr +
            (it.tag_sa()) * tag_sa +
            (it.tag_so()) * tag_so +
            (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
            (it.aktzweck_work()) * aktzweck_work +
            (it.aktzweck_education()) * aktzweck_education +
            (it.aktzweck_shopping()) * aktzweck_shopping +
            (it.aktzweck_transport()) * aktzweck_transport +
            (it.wochenzbudget_zweck_kat1()) * wochenzbudget_zweck_kat1 +
            (it.wochenzbudget_zweck_kat2()) * wochenzbudget_zweck_kat2 +
            (it.wochenzbudget_zweck_kat3()) * wochenzbudget_zweck_kat3 +
            (it.wochenzbudget_zweck_kat4()) * wochenzbudget_zweck_kat4 +
            (it.wochenzbudget_zweck_kat5()) * wochenzbudget_zweck_kat5 +
            (it.tourhat1akt()) * tourhat1akt +
            (it.tourhat2akt()) * tourhat2akt +
            (it.tourhat3akt()) * tourhat3akt +
            (it.taghat1akt()) * taghat1akt +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.taghat1tour()) * taghat1tour +
            (it.taghat2touren()) * taghat2touren +
            (it.beruf_vollzeit()) * beruf_vollzeit +
            (it.beruf_teilzeit()) * beruf_teilzeit +
            (it.beruf_schueler_azubi()) * beruf_schueler_azubi +
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