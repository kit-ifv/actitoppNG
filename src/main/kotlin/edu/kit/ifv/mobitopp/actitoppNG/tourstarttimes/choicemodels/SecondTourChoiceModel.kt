package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DurationDay
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10O
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

/**
 * Return an int representative of the duration 0..<2 hours 2..<4 and 4..<6. Do so to get a table jump
 * in the when statement, rather than comparisons.
 */
private fun deduceDurationInt(duration: Duration): Int {
    return (duration.inWholeHours / 2L).toInt()
}

/**
 * Encodes the activity type to an integer based on the original enum values as ordinal:
 *
 *     EDUCATION 0,
 *     HOME 1,
 *     LEISURE 2,
 *     SHOPPING 3,
 *     TRANSPORT 4,
 *     WORK 5;
 *
 *     This will break if the enum changes order, in that case you must adapt the utility function.
 */
private fun deduceActivityTypeCode(activityType: ActivityType): Int {
    return activityType.ordinal
}

private fun deduceDay(durationDay: DurationDay): Int {
    return durationDay.weekday.value
}

/**
 * This is the original enum orientation that i assume
 *     0 FULLTIME(1),
 *     1 PARTTIME(2),
 *     2 UNOCCUPIED(3),
 *     3 STUDENT(4),
 *     4  VOCATIONAL(5),
 *     5  HOUSEKEEPER(6),
 *     6  RETIRED(7),
 *     7  UNKNOWN_21(21),
 *     8  MARGINAL(22),
 *     9  STUDENT_PRIMARY(40),
 *     10 STUDENT_SECONDARY(41),
 *     11 STUDENT_TERTIARY(42),
 *     12 DEFINITELY_UNKNOWN(Int.MIN_VALUE);
 *
 */
private fun deduceEmployment(employment: Employment): Int {
    return employment.ordinal
}
private val standardUtilityFunction10O: ParameterStep10O.(MainDurationAlternative) -> Double = {

    val previousDayTourEndHour = it.dayPlan.endOfPreviousTour(it.tourPlan)
    val activityDurationInTourCode = deduceDurationInt(it.tourPlan.activityDurations)
    val activityCode = deduceActivityTypeCode(it.activityType)
    val dayCode = deduceDay(it.dayPlan.durationDay)
    val employmentCode = deduceEmployment(it.person.employment)
    val activitiesOfDay = it.dayPlan.amountOfActivities
    val numTours = it.dayPlan.tourPlans.size
    base +
            when(employmentCode) {
                0 -> beruf_vollzeit
                1, 7 , 8 -> beruf_teilzeit
                3, 9, 10, 11 -> beruf_schueler
                4 -> beruf_azubi
                else -> .0
            } +
            when(activityCode) {
                0 -> tourtyp_education
                3 -> tourtyp_shopping
                4 -> tourtyp_transport
                5 -> tourtyp_work
                else -> .0

            } +
            when(dayCode) {
                5 -> tag_fr
                6 -> tag_sa
                7 -> tag_so
                else -> .0
            } +
            when(activitiesOfDay) {
                2 -> taghat2akt
                3 -> taghat3akt
                else -> .0
            } +
            when(numTours) {
                2 -> taghat2touren
                3 -> taghat3touren
                else -> .0
            } +
            when(activityDurationInTourCode) {
                0 -> dauer_akt_in_tour_0bis2std
                1 -> dauer_akt_in_tour_2bis4std
                2 -> dauer_akt_in_tour_4bis6std
                else -> .0
            } +

            (it.touristhaupttour()) * touristhaupttour +
            when (previousDayTourEndHour.inWholeHours) {
                12L -> endetourvorher_Std_12
                13L -> endetourvorher_Std_13
                14L -> endetourvorher_Std_14
                15L -> endetourvorher_Std_15
                16L -> endetourvorher_Std_16
                17L -> endetourvorher_Std_17
                18L -> endetourvorher_Std_18
                else -> .0
            }


}

context(params: PlanGenerationParameters)
val SECOND_TOUR_HISTOGRAM
    get() = params.secondTourHistogramParams.generateHistogram(
        ArrayHistogram.fromResource(

            identifier = Identifier.SECOND_TOUR_START_TIME
        ),
        standardUtilityFunction10O
    )