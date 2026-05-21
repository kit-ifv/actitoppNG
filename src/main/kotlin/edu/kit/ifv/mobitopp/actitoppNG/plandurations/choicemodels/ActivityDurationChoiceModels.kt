package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8J
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


private val minorFunction: ParameterStep8J.(MainDurationAlternative) -> Double = {

    val durationMainAct = it.dayPlan.durationOfMainActivities
    val averageTimeOfActivity = it.dayPlan.getBudget(it.activityType)
    base +
            when (durationMainAct) {
                in 4.hours..<6.hours -> dauer_hauptakt_tag_4bis6std
                in 6.hours..<8.hours -> dauer_hauptakt_tag_6bis8std
                in 8.hours..<10.hours -> dauer_hauptakt_tag_8bis10std
                in 10.hours..<12.hours -> dauer_hauptakt_tag_10bis12std
                in 12.hours..<14.hours -> dauer_hauptakt_tag_12bis14std
                in 14.hours..<Int.MAX_VALUE.hours -> dauer_hauptakt_tag_ueber14std
                else -> .0
            } +
            when(averageTimeOfActivity) {
                in 1.minutes..<15.minutes -> mittl_zeit_akt_1bis14min
                in 15.minutes..<30.minutes -> mittl_zeit_akt_15bis29min
                in 30.minutes..<60.minutes -> mittl_zeit_akt_30bis59min
                in 60.minutes..<120.minutes -> mittl_zeit_akt_60bis119min
                else -> .0
            } +
            (it.tag_so()) * tag_so +
            (it.aktzweck_work()) * aktzweck_work +
            (it.aktzweck_education()) * aktzweck_education +
            (it.aktzweck_shopping()) * aktzweck_shopping +
            (it.aktzweck_transport()) * aktzweck_transport +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.taghat4akt()) * taghat4akt +
            (it.taghat5akt()) * taghat5akt +
            (it.taghat6akt()) * taghat6akt +
            (it.tourtyp_work()) * tourtyp_work +
            (it.tourtyp_education()) * tourtyp_education +
            (it.tourtyp_shopping()) * tourtyp_shopping +
            (it.tourtyp_transport()) * tourtyp_transport +
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

            when(averageTimeOfActivity) {
                in 1.minutes..<15.minutes -> mittl_zeit_akt_1bis14min
                in 15.minutes..<30.minutes -> mittl_zeit_akt_15bis29min
                in 30.minutes..<60.minutes -> mittl_zeit_akt_30bis59min
                in 60.minutes..<120.minutes -> mittl_zeit_akt_60bis119min
                in 120.minutes..<180.minutes -> mittl_zeit_akt_120bis179min
                in 180.minutes..<240.minutes -> mittl_zeit_akt_180bis239min
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
            when(averageTimeOfActivity) {
                in 60.minutes..<120.minutes -> mittl_zeit_akt_60bis119min
                in 120.minutes..<180.minutes -> mittl_zeit_akt_120bis179min
                in 180.minutes..<240.minutes -> mittl_zeit_akt_180bis239min
                in 240.minutes..<300.minutes -> mittl_zeit_akt_240bis299min
                in 300.minutes..<360.minutes -> mittl_zeit_akt_300bis359min
                in 360.minutes..<420.minutes -> mittl_zeit_akt_360bis419min
                in 420.minutes..<480.minutes -> mittl_zeit_akt_420bis479min
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