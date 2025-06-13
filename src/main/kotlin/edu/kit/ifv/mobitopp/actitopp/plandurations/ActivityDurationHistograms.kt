package edu.kit.ifv.mobitopp.actitopp.plandurations

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.changes.Category
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.enums.Employment
import edu.kit.ifv.mobitopp.actitopp.enums.isParttime
import edu.kit.ifv.mobitopp.actitopp.enums.isStudent
import edu.kit.ifv.mobitopp.actitopp.enums.isStudentOrAzubi
import edu.kit.ifv.mobitopp.actitopp.modernization.Activity
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.DayPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.TourPlan
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.timebudgets.TimeBudgets
import edu.kit.ifv.mobitopp.actitopp.timebudgets.indexOfSearch
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ChoiceSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.UtilityFunction
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.selectNew
import edu.kit.ifv.mobitopp.actitopp.utils.Position
import edu.kit.ifv.mobitopp.actitopp.utils.sumOf
import java.nio.file.Path
import java.time.DayOfWeek
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


/**
 * Contains the histograms originally found in 8C. They are assigning the duration of the first main activity of
 * a day, Henceforth referenced as "Lead activity"
 */
open class ActivityDurationHistograms<P>(
    // TODO histograms could be a sorted Set, since they are supposed to cover respective ranges.
    open val histograms: List<ArrayHistogram>,
    open val choiceModel: ParametrizedDiscreteChoiceModel<ArrayHistogram, MainDurationSituation, P>,
    val emergencyBehaviour: (ClosedRange<Duration>, Double) -> Duration = { range, rng ->

        ((range.endInclusive - range.start) * rng + range.start).inWholeMinutes.minutes
    },
) {
    fun categoryFor(duration: Duration): Category {
        return histograms.first { duration in it }.categoryIndex
    }

    fun categoryFor(int: Int): Category {
        return histograms.first { int in it }.categoryIndex
    }

    /**
     * Find the proper histogram, select between histogram and neighbors, where main histogram gets a 1.1 boost.
     * Select duration from selected histogram.
     */
    fun chooseHistogramFromNeighbors(
        randomValue: Double,
        duration: Duration,
        bounds: ClosedRange<Duration>,
        converter: (ArrayHistogram) -> MainDurationSituation,
    ): ArrayHistogram {
        val index = histograms.binarySearch { it.compareTo(duration.inWholeMinutes.toInt()) }.indexOfSearch()
        val mainHistogram = histograms[index]
        val previousHistogram = histograms.getOrNull(index - 1)
        val nextHistogram = histograms.getOrNull(index + 1)
        val selectedHistogram = choiceModel.selectNew(randomValue, converter) {
            previousHistogram?.let {
                if (it.intersects(bounds)) {
                    option(it)
                }

            }
            if (mainHistogram.intersects(bounds)) {
                option(mainHistogram) { original ->
                    UtilityFunction { a, b ->
                        1.1 * original.calculateUtility(a, b)
                    }
                }
            }

            nextHistogram?.let {
                if (it.intersects(bounds)) {
                    option(it)
                }

            }
        }
        return selectedHistogram
    }

    fun select(rngHelper: RNGHelper, converter: (ArrayHistogram) -> MainDurationSituation): Duration {
        return choiceModel.select(rngHelper.randomValue, converter).select(rngHelper.randomValue)
    }

    fun selectHistogram(
        rnd1: Double,
        bounds: ClosedRange<Duration>,
        converter: (ArrayHistogram) -> MainDurationSituation,
    ): ArrayHistogram? {
        val options = histograms.filter { it.end >= bounds.start && it.start <= bounds.endInclusive }.toSet()
        // It can happen that no histogram fits, because they are trimmed. The default behaviour of legacy actitopp is to return a random value.
        if (options.isEmpty()) {
            return null
        }
        return choiceModel.select(options, rnd1, converter)
    }

    fun select(
        rnd1: Double,
        rnd2: Double,
        bounds: ClosedRange<Duration>,
        converter: (ArrayHistogram) -> MainDurationSituation,
    ): Duration {

        val concreteHistogram = selectHistogram(rnd1, bounds, converter) ?: return emergencyBehaviour(bounds, rnd2)

        val output = concreteHistogram.selectInt(
            rnd2,
            bounds.start.inWholeMinutes.toInt(),
            bounds.endInclusive.inWholeMinutes.toInt()
        )
        return output
    }

    /**
     * The legacy code defaces,modifies and otherwise corrupts the histograms for each person individually. Thus, we need a taintable copy.
     */
    fun taint(): TaintedActivityDurationHistograms<P> {
        return TaintedActivityDurationHistograms(this)
    }

}

class TaintedActivityDurationHistograms<P>(
    val original: ActivityDurationHistograms<P>,
) {
    val histograms = original.histograms
    val choiceModel = original.choiceModel
    val taintedHistograms = histograms.associateWith { it.copy() }


    fun selectAndTaint(
        rngNumberOne: Double,
        rngNumberTwo: Double,
        bounds: ClosedRange<Duration>,
        duration: Duration,
        converter: (ArrayHistogram) -> MainDurationSituation,
    ): Duration {

        val selectedHistogram = original.chooseHistogramFromNeighbors(rngNumberOne, duration, bounds, converter)
        val taint = taintedHistograms.getValue(selectedHistogram)


        return taint.select(rngNumberTwo, bounds).also {
            taint.modify(it.inWholeMinutes.toInt())
        }
    }

    fun select(
        rnd1: Double,
        rnd2: Double,
        bounds: ClosedRange<Duration>,
        converter: (ArrayHistogram) -> MainDurationSituation,
    ): Duration {

        val histogram =
            original.selectHistogram(rnd1, bounds, converter) ?: return original.emergencyBehaviour(bounds, rnd2)
        val taint = taintedHistograms.getValue(histogram)
        return taint.select(rnd2, bounds)
    }
}

enum class Identifier(val id: String) {
    LEAD_ACTIVITY_DURATION("8C"),
    MAJOR_ACTIVITY_DURATION("8E"),
    MINOR_ACTIVITY_DURATION("8K"),

    FIRST_TOUR_START_TIME("10N"),
    SECOND_TOUR_START_TIME("10P"),
    OTHER_TOUR_START_TIME("10T")
}

fun durationHistogramsFromResourcePath(
    identifier: Identifier,
    path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh"),
): List<ArrayHistogram> {

    val globPattern = "${identifier.id}_KAT_*.csv"
    return path.listDirectoryEntries(globPattern).sortedBy {
        it.name.removePrefix("${identifier.id}_KAT_")
            .removeSuffix(".csv")
            .toInt()
    }.map { ArrayHistogram.fromPath(it) }

}

fun <P, T> P.generateHistogram(
    histograms: List<ArrayHistogram>,
    utilityFunction: T.(MainDurationSituation) -> Double,
    converter: (Int, P) -> (P) -> T,
): ActivityDurationHistograms<P> {

    val mapper = histograms.withIndex().map { (index, histogram) ->
        histogram to
                converter(index, this)
    }
    val choiceModel = ModifiableDiscreteChoiceModel<ArrayHistogram, MainDurationSituation, P>(
        AllocatedLogit.create {
            bulk(mapper) {
                utilityFunction(this, it)
            }
        }
    ).initializeWithParameters(this)

    return ActivityDurationHistograms(histograms, choiceModel)

}


open class PlanSituation<P : Any>(
    override val choice: P,

    input: MobilityPlanInputs,

    ) : ChoiceSituation<P>() {

    val mobilityPlan: MobilityPlan = input.mobilityPlan
    val dayPlan: DayPlan = input.dayPlan
    val tourPlan: TourPlan = input.tourPlan
    val activity: Activity = input.activity
    val planTimeBudgets: TimeBudgets = input.mobilityPlan.timeBudgets
    val person: IPerson = input.person
    val isLastTourOfDay: Boolean = input.isLastTourOfDay
    val activityType: ActivityType = activity.activityType

    // TODO communicate clearly that this interval check is an open end range, because the legacy code doesnt communicate that
    fun dauer_akt_tag_4bis6std(): Boolean = dayPlan.durationOfActivities() in 4.hours..<6.hours
    fun dauer_akt_tag_6bis8std(): Boolean = dayPlan.durationOfActivities() in 6.hours..<8.hours
    fun dauer_akt_tag_8bis10std(): Boolean = dayPlan.durationOfActivities() in 8.hours..<10.hours
    fun dauer_akt_tag_10bis12std(): Boolean = dayPlan.durationOfActivities() in 10.hours..<12.hours
    fun dauer_akt_tag_12bis14std(): Boolean = dayPlan.durationOfActivities() in 12.hours..<14.hours

    fun anztagemit_tourennachht(): Int {
        // TODO this number is always fixed and should be precalculated in the mobility plan
        return mobilityPlan.dayPlans.count { it.tourPlans.last().position == Position.AFTER }
    }

    fun householdHasYouths() = person.children_u18 > 0
    fun anztagemit_tourenvorht(): Int {
        // TODO this number is also fixed once the mobility Plan is formulated.
        return mobilityPlan.dayPlans.count { it.tourPlans.first().position == Position.BEFORE }

    }

    fun anztourenamtag(): Int {
        return dayPlan.tourPlans.size
    }

    fun tour3destages() = tourPlan == dayPlan.tourPlans.getOrNull(2)
    fun isStudent() = person.employment.isStudent()
    fun isVocational() = person.employment == Employment.VOCATIONAL
    fun isAged10to17() = person.age in 10..17
    fun haupttour_work() = dayPlan.mainActivityType() == ActivityType.WORK
    fun haupttour_education() = dayPlan.mainActivityType() == ActivityType.EDUCATION
    fun touristhaupttour() = tourPlan.position == Position.MAIN
    fun dauer_hauptakt_tag_4bis6std(): Boolean {
        return dayPlan.durationOfMainActivities in 4.hours..<6.hours
    }

    fun dauer_hauptakt_tag_6bis8std(): Boolean {
        return dayPlan.durationOfMainActivities in 6.hours..<8.hours
    }

    fun dauer_hauptakt_tag_8bis10std(): Boolean {
        return dayPlan.durationOfMainActivities in 8.hours..<10.hours
    }

    fun dauer_hauptakt_tag_10bis12std(): Boolean {
        return dayPlan.durationOfMainActivities in 10.hours..<12.hours
    }

    fun dauer_hauptakt_tag_12bis14std(): Boolean {
        return dayPlan.durationOfMainActivities in 12.hours..<14.hours
    }

    fun dauer_hauptakt_tag_ueber14std(): Boolean {
        return dayPlan.durationOfMainActivities >= 14.hours
    }

    fun mittl_zeit_akt_1bis14min(): Boolean {
        return dayPlan.getBudget(activityType) in 1.minutes..<15.minutes
    }

    fun mittl_zeit_akt_15bis29min(): Boolean {
        return dayPlan.getBudget(activityType) in 15.minutes..<30.minutes
    }

    fun mittl_zeit_akt_30bis59min(): Boolean {
        return dayPlan.getBudget(activityType) in 30.minutes..<60.minutes
    }

    fun mittl_zeit_akt_60bis119min(): Boolean {
        return dayPlan.getBudget(activityType) in 60.minutes..<120.minutes
    }

    fun mittl_zeit_akt_120bis179min(): Boolean {
        return dayPlan.getBudget(activityType) in 120.minutes..<180.minutes
    }

    fun mittl_zeit_akt_180bis239min(): Boolean {
        return dayPlan.getBudget(activityType) in 180.minutes..<240.minutes
    }

    fun mittl_zeit_akt_240bis299min(): Boolean {
        return dayPlan.getBudget(activityType) in 240.minutes..<300.minutes
    }

    fun mittl_zeit_akt_300bis359min(): Boolean {
        return dayPlan.getBudget(activityType) in 300.minutes..<360.minutes
    }

    fun mittl_zeit_akt_360bis419min(): Boolean {
        return dayPlan.getBudget(activityType) in 360.minutes..<420.minutes
    }

    fun mittl_zeit_akt_420bis479min(): Boolean {
        return dayPlan.getBudget(activityType) in 420.minutes..<480.minutes

    }

    fun tag_so(): Boolean {
        return dayPlan.durationDay.weekday == DayOfWeek.SUNDAY
    }

    fun aktzweck_work(): Boolean {
        return activityType == ActivityType.WORK
    }

    fun aktzweck_education(): Boolean {
        return activityType == ActivityType.EDUCATION
    }

    fun aktzweck_shopping(): Boolean {
        return activityType == ActivityType.SHOPPING
    }

    fun aktzweck_transport(): Boolean {
        return activityType == ActivityType.TRANSPORT
    }

    fun taghat2akt(): Boolean {
        return dayPlan.amountOfActivities == 2
    }

    fun taghat3akt(): Boolean {
        return dayPlan.amountOfActivities == 3
    }

    fun taghat4akt(): Boolean {
        return dayPlan.amountOfActivities == 4
    }

    fun taghat5akt(): Boolean {
        return dayPlan.amountOfActivities == 5
    }

    fun taghat6akt(): Boolean {
        return dayPlan.amountOfActivities == 6
    }

    fun tourtyp_work(): Boolean {
        return tourPlan.mainActivity.activityType == ActivityType.WORK
    }

    fun tourtyp_education(): Boolean {
        return tourPlan.mainActivity.activityType == ActivityType.EDUCATION
    }

    fun tourtyp_shopping(): Boolean {
        return tourPlan.mainActivity.activityType == ActivityType.SHOPPING
    }

    fun tourtyp_transport(): Boolean {
        return tourPlan.mainActivity.activityType == ActivityType.TRANSPORT
    }

    fun tourhat2akt(): Boolean {
        return tourPlan.size == 2
    }

    fun taghat1tour(): Boolean {
        return dayPlan.tourPlans.size == 1
    }

    fun taghat2touren(): Boolean {
        return dayPlan.tourPlans.size == 2
    }

    fun taghat3touren(): Boolean {
        return dayPlan.tourPlans.size == 3
    }

    fun aktliegtvorhauptakt(): Boolean {
        return activity.position == Position.BEFORE
    }

    fun tourliegtvorhaupttour(): Boolean {
        return tourPlan.position == Position.BEFORE
    }

    fun tourliegtnachhaupttour(): Boolean {
        return tourPlan.position == Position.AFTER
    }

    fun tag_fr(): Boolean {
        return dayPlan.durationDay.weekday == DayOfWeek.FRIDAY
    }

    fun tag_sa(): Boolean {
        return dayPlan.durationDay.weekday == DayOfWeek.SATURDAY
    }

    fun dauer_akt_vorht_tag_1bis120(): Boolean {
        return dayPlan.tourPlans.filter { it.position == Position.BEFORE }
            .sumOf { it.activityDurationsWithTrips } in 1.minutes..<120.minutes
    }

    fun anztourenvorhaupttour() = dayPlan.tourPlans.count { it.position == Position.BEFORE }
    fun anztourennachhaupttour() = dayPlan.tourPlans.count { it.position == Position.AFTER }
    fun endetourvorher_Std_12() = dayPlan.endOfPreviousTour(tourPlan) in 12.hours..<13.hours

    fun endetourvorher_Std_13() = dayPlan.endOfPreviousTour(tourPlan) in 13.hours..<14.hours

    fun endetourvorher_Std_14() = dayPlan.endOfPreviousTour(tourPlan) in 14.hours..<15.hours

    fun endetourvorher_Std_15() = dayPlan.endOfPreviousTour(tourPlan) in 15.hours..<16.hours

    fun endetourvorher_Std_16() = dayPlan.endOfPreviousTour(tourPlan) in 16.hours..<17.hours

    fun endetourvorher_Std_17() = dayPlan.endOfPreviousTour(tourPlan) in 17.hours..<18.hours

    fun endetourvorher_Std_18() = dayPlan.endOfPreviousTour(tourPlan) in 18.hours..<19.hours

    fun endetourvorher_Std_19() = dayPlan.endOfPreviousTour(tourPlan) in 19.hours..<20.hours

    fun endetourvorher_Std_20() = dayPlan.endOfPreviousTour(tourPlan) in 20.hours..<21.hours

    fun dauer_akt_in_tour_0bis2std() = tourPlan.activityDurations in 0.hours..<2.hours
    fun dauer_akt_in_tour_2bis4std() = tourPlan.activityDurations in 2.hours..<4.hours
    fun dauer_akt_in_tour_4bis6std() = tourPlan.activityDurations in 4.hours..<6.hours
    fun dauer_akt_in_tour_6bis8std() = tourPlan.activityDurations in 6.hours..<8.hours
    fun dauer_akt_in_tour_8bis10std() = tourPlan.activityDurations in 8.hours..<10.hours
    fun dauer_akt_in_tour_10bis12std() = tourPlan.activityDurations in 10.hours..<12.hours
    fun anzaktwieanztagemitzweck(): Boolean {
        return mobilityPlan.regularActivities[activityType] ?: false
    }

    fun wochenzbudget_zweck_kat1(): Boolean {
        return planTimeBudgets.getCategory(activityType).matches(1)
    }

    fun wochenzbudget_zweck_kat2(): Boolean {
        return planTimeBudgets.getCategory(activityType).matches(2)
    }

    fun wochenzbudget_zweck_kat3(): Boolean {
        return planTimeBudgets.getCategory(activityType).matches(3)
    }

    fun wochenzbudget_zweck_kat4(): Boolean {
        return planTimeBudgets.getCategory(activityType).matches(4)
    }

    fun wochenzbudget_zweck_kat5(): Boolean {
        return planTimeBudgets.getCategory(activityType).matches(5)
    }

    fun tourhat1akt(): Boolean {
        return tourPlan.size == 1
    }

    fun tourhat3akt(): Boolean {
        return tourPlan.size == 3
    }

    fun letztetourdestages(): Boolean {
        return isLastTourOfDay
    }

    fun beruf_vollzeit(): Boolean {
        return person.employment == Employment.FULLTIME
    }

    fun beruf_teilzeit(): Boolean {
        return person.employment.isParttime()
    }

    fun beruf_schueler_azubi(): Boolean {
        return person.employment.isStudentOrAzubi()
    }

    fun taghat1akt(): Boolean {
        return dayPlan.amountOfActivities == 1
    }
}

class MainDurationSituation(
    choice: ArrayHistogram,
    input: MobilityPlanInputs,
) : PlanSituation<ArrayHistogram>(
    choice,
    input
)

open class BooleanDecisionSituation(
    choice: Boolean,
    input: MobilityPlanInputs,
) : PlanSituation<Boolean>(
    choice,
    input
)

class BooleanDecisionWithPreferenceCategory(
    choice: Boolean,
    input: MobilityPlanInputs, val preferredHistogram: ArrayHistogram,
) : BooleanDecisionSituation(
    choice,
    input
) {
    fun std_start_T1_6_7_Uhr(): Boolean {
        return preferredHistogram.start in (6.hours - 15.minutes)..(6.hours + 15.minutes) // SOme buffer because the histograms could theroretically not have an entry precisely for 6 hours
    }

    fun std_start_T1_7_8_Uhr(): Boolean {
        return preferredHistogram.start in (7.hours - 15.minutes)..(7.hours + 15.minutes) // SOme buffer
    }
}

