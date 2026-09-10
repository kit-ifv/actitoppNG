package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes

import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.performance.TimeBlockEncoder
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10S
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParametersStep10S
import edu.kit.ifv.mobitopp.actitoppNG.utils.minus
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import kotlin.random.Random
import kotlin.time.Duration

data class UtilFunction10SCharacteristic(
    val previousDayTourEndHour: Int,
    val dayActivityDurationTwoHourBlock: Int,
    val tourIsMainTour: Boolean,
    val toursPerDay: Int,
    val isThirdTourOfTheDay: Boolean,
    val isStudent: Boolean,
) {
    constructor(alternative: MainDurationAlternative): this(
        previousDayTourEndHour = alternative.dayPlan.endOfPreviousTour(alternative.tourPlan).inWholeHours.toInt(),
        dayActivityDurationTwoHourBlock = TimeBlockEncoder.convert(alternative.dayPlan.durationOfActivities()),
        tourIsMainTour= alternative.touristhaupttour(),
        toursPerDay= alternative.anztourenamtag(),
        isThirdTourOfTheDay= alternative.tour3destages(),
        isStudent= alternative.person.employment.isStudent(),
    )
}

private val standardUtilityFunction10S: ParameterStep10S.(UtilFunction10SCharacteristic) -> Double = {
    base +
            (it.tourIsMainTour) * touristhaupttour +
            (it.toursPerDay) * anztourenamtag +
            (it.isThirdTourOfTheDay) * tour3destages +
            when (it.dayActivityDurationTwoHourBlock) {
                TimeBlockEncoder.In4to6Hours -> dauer_akt_tag_4bis6std
                TimeBlockEncoder.In6to8Hours-> dauer_akt_tag_6bis8std
                TimeBlockEncoder.In8to10Hours -> dauer_akt_tag_8bis10std
                TimeBlockEncoder.In10to12Hours -> dauer_akt_tag_10bis12std
                TimeBlockEncoder.In12to14Hours -> dauer_akt_tag_12bis14std
                else -> .0
            } +
            when (it.previousDayTourEndHour) {
                12 -> endetourvorher_Std_12
                13 -> endetourvorher_Std_13
                14 -> endetourvorher_Std_14
                15 -> endetourvorher_Std_15
                16 -> endetourvorher_Std_16
                17 -> endetourvorher_Std_17
                18 -> endetourvorher_Std_18
                19 -> endetourvorher_Std_19
                20 -> endetourvorher_Std_20
                else -> .0
            } +
            (it.isStudent) * beruf_schueler
}


class TourStartByHistogramsRelative(

    private val startTimeHistograms: ActivityDurationHistograms,
) :
    SelectTourStart {
    context(rng: Random)
    override fun selectStartTime(input: MobilityPlanInputs): Duration {
        val bounds = input.dayPlan.startTimeBoundsFor(input.tourPlan, disregardDayEnd = input.isLastDay)
        val startTime = bounds.start
        val relativeBounds = bounds - startTime
        return startTimeHistograms.select(relativeBounds, MainDurationAlternative(input)) + startTime
    }

    companion object {
        fun standard(): TourStartByHistogramsRelative {
            return TourStartByHistogramsRelative(OTHER_TOUR_HISTOGRAM)
        }
    }
}


val OTHER_TOUR_HISTOGRAM = ParametersStep10S.generateHistogram(
    ArrayHistogram.fromResource(
        identifier = Identifier.OTHER_TOUR_START_TIME
    ),
    { UtilFunction10SCharacteristic(it) },
    standardUtilityFunction10S
)