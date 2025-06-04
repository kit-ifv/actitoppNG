package edu.kit.ifv.mobitopp.actitopp.steps.step10

import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.steps.step7.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.steps.step8.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.steps.step8.Identifier
import edu.kit.ifv.mobitopp.actitopp.steps.step8.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.steps.step8.durationHistogramsFromResourcePath
import edu.kit.ifv.mobitopp.actitopp.steps.step8.generateHistogram
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times
import kotlin.time.Duration


private val standardUtilityFunction10M: ParameterStep10M.(MainDurationSituation) -> Double = {
    base +
            (it.beruf_vollzeit()) * beruf_vollzeit +
            (it.beruf_teilzeit()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.isVocational()) * beruf_azubi +
            (it.tourtyp_work()) * tourtyp_work +
            (it.tourtyp_education()) * tourtyp_education +
            (it.tourtyp_shopping()) * tourtyp_shopping +
            (it.tourtyp_transport()) * tourtyp_transport +
            (it.tag_sa()) * tag_sa +
            (it.tag_so()) * tag_so +
            (it.dauer_akt_tag_4bis6std()) * dauer_akt_tag_4bis6std +
            (it.dauer_akt_tag_6bis8std()) * dauer_akt_tag_6bis8std +
            (it.dauer_akt_tag_8bis10std()) * dauer_akt_tag_8bis10std +
            (it.dauer_akt_tag_10bis12std()) * dauer_akt_tag_10bis12std +
            (it.dauer_akt_tag_12bis14std()) * dauer_akt_tag_12bis14std +
            (it.taghat1akt()) * taghat1akt +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.taghat1tour()) * taghat1tour +
            (it.taghat2touren()) * taghat2touren +
            (it.haupttour_work()) * haupttour_work +
            (it.haupttour_education()) * haupttour_education +
            (it.touristhaupttour()) * touristhaupttour


}
val FIRST_TOUR_HISTOGRAM: ActivityDurationHistograms<ParameterCollectionStep10M> = ParametersStep10M.generateHistogram(
    durationHistogramsFromResourcePath(Identifier.FIRST_TOUR_START_TIME),
    standardUtilityFunction10M
) { index, objec ->
    { objec.parameters[index] }
}



class TourStartWithPreference<P>(private val rng: RNGKeeper, val categoryID: String, val  weightedDrawID: String,  private val startTimeHistograms: ActivityDurationHistograms<P>, override val preferredTourStart: ArrayHistogram? = null) :
    SelectTourStartWithPreference {

    override fun selectStartTime(input: MobilityPlanInputs, preferredTourStart: ArrayHistogram?): Duration {
        val bounds = input.dayPlan.startTimeBoundsFor(input.tourPlan)

        val rnd1 = rng.pull(categoryID)
        val rnd2 = rng.pull(weightedDrawID)

        preferredTourStart?.let {
            if (it.intersects(bounds)) {
                // In this instance we discard rnd1 because it is not required to find out that we want to use our preferred histogram. The legacy code will always generate the random number, so for consistency we always need to consume it
                return it.select(
                    rnd2,
                    bounds.start,
                    bounds.endInclusive
                )
            }

        }
        return startTimeHistograms.select(rnd1, rnd2, bounds) {
            MainDurationSituation(it, input)
        }
    }
}