package edu.kit.ifv.mobitopp.actitopp.tourstarttimes

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitopp.plandurations.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.plandurations.durationHistogramsFromResourcePath
import edu.kit.ifv.mobitopp.actitopp.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.parameters.ParameterCollectionStep10S
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.parameters.ParameterStep10S
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.parameters.ParametersStep10S
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times
import kotlin.time.Duration


private val standardUtilityFunction10S: ParameterStep10S.(MainDurationSituation) -> Double = {
    base +

            (it.touristhaupttour()) * touristhaupttour +
            (it.anztourenamtag()) * anztourenamtag +
            (it.tour3destages()) * tour3destages +
            (it.dauer_akt_tag_4bis6std()) * dauer_akt_tag_4bis6std +
            (it.dauer_akt_tag_6bis8std()) * dauer_akt_tag_6bis8std +
            (it.dauer_akt_tag_8bis10std()) * dauer_akt_tag_8bis10std +
            (it.dauer_akt_tag_10bis12std()) * dauer_akt_tag_10bis12std +
            (it.dauer_akt_tag_12bis14std()) * dauer_akt_tag_12bis14std +
            (it.endetourvorher_Std_12()) * endetourvorher_Std_12 +
            (it.endetourvorher_Std_13()) * endetourvorher_Std_13 +
            (it.endetourvorher_Std_14()) * endetourvorher_Std_14 +
            (it.endetourvorher_Std_15()) * endetourvorher_Std_15 +
            (it.endetourvorher_Std_16()) * endetourvorher_Std_16 +
            (it.endetourvorher_Std_17()) * endetourvorher_Std_17 +
            (it.endetourvorher_Std_18()) * endetourvorher_Std_18 +
            (it.endetourvorher_Std_19()) * endetourvorher_Std_19 +
            (it.endetourvorher_Std_20()) * endetourvorher_Std_20 +
            (it.isStudent()) * beruf_schueler

}

operator fun ClosedRange<Duration>.minus(duration: Duration): ClosedRange<Duration> {
    return (start - duration)..(endInclusive - duration)
}

class TourStartByHistogramsRelative<P>(
    private val rng: RNGHelper,
    private val startTimeHistograms: ActivityDurationHistograms<P>,
) :
    SelectTourStart {
    override fun selectStartTime(input: MobilityPlanInputs): Duration {
        val bounds = input.dayPlan.startTimeBoundsFor(input.tourPlan, disregardDayEnd = input.isLastDay)
        val startTime = bounds.start
        val relativeBounds = bounds - startTime
        val rnd1 = rng.randomValue
        val rnd2 = rng.randomValue
        return startTimeHistograms.select(rnd1, rnd2, relativeBounds) {
            MainDurationSituation(it, input)
        } + startTime
    }

    companion object {
        fun standard(rng: RNGHelper): TourStartByHistogramsRelative<ParameterCollectionStep10S> {
            return TourStartByHistogramsRelative(rng, OTHER_TOUR_HISTOGRAM)
        }
    }
}


val OTHER_TOUR_HISTOGRAM = ParametersStep10S.generateHistogram(
    durationHistogramsFromResourcePath(Identifier.OTHER_TOUR_START_TIME),
    standardUtilityFunction10S
) { index, obj ->
    { obj.parameters[index] }
}