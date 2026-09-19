package edu.kit.ifv.mobitopp.actitoppNG.plandurations


import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.modernization.Activity
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.DayPlan
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.TourPlan
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.CorruptorFactory
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.TimeBudgets
import edu.kit.ifv.mobitopp.actitoppNG.utils.Position
import edu.kit.ifv.mobitopp.actitoppNG.utils.indexOfSearch
import edu.kit.ifv.mobitopp.actitoppNG.utils.sumOf
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromList
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


interface HistogramSelector {

    context(rng: Random)
    fun chooseHistogramFromNeighbors(
        duration: Duration,
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram

    context(rng: Random)
    fun select(converter: MainDurationAlternative): Duration

    context(rng: Random)
    fun selectHistogram(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram?


    context(rng: Random)
    fun selectSingular(converter: MainDurationAlternative): ArrayHistogram
    context(converter: MainDurationAlternative)
    fun probabilitiesSingular():  Map<ArrayHistogram, Double>

    context(rng: Random)
    fun select(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): Duration

    fun taint(): TaintedHistogramSelector
}


/**
 * Contains the histograms originally found in 8C. They are assigning the duration of the first main activity of
 * a day, Henceforth referenced as "Lead activity"
 */
open class ActivityDurationHistograms(
    // TODO histograms could be a sorted Set, since they are supposed to cover respective ranges.
    open val histograms: List<ArrayHistogram>,
    open val choiceModel: FixedChoiceModel<ArrayHistogram, MainDurationAlternative>,
    val emergencyBehaviour:  (ClosedRange<Duration>, Random) -> Duration = { range, rng ->

        ((range.endInclusive - range.start) * rng.nextDouble() + range.start).inWholeMinutes.minutes
    },
) : HistogramSelector{

    /**
     * Find the proper histogram, select between histogram and neighbors, where main histogram gets a 1.1 boost.
     * Select duration from selected histogram.
     */
    context(rng: Random)
    override fun chooseHistogramFromNeighbors(
        duration: Duration,
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram {
        val index = histograms.binarySearch { it.compareTo(duration.inWholeMinutes.toInt()) }.indexOfSearch()
        val mainHistogram = histograms[index]
        val previousHistogram = histograms.getOrNull(index - 1)
        val nextHistogram = histograms.getOrNull(index + 1)

        val choices = listOfNotNull(previousHistogram, mainHistogram, nextHistogram).filter { it.intersects(bounds) }.toSet()
        require(choices.isNotEmpty()) {
            "The choice set is empty, this happens because the duration is $duration and the bounds $bounds, ${histograms.map { it.toString() }}"
        }

        return context(converter) {
            choiceModel.selectInjected(choices, injections = mapOf(mainHistogram to { d: Double -> d * 1.1 }))
        }

    }
    context(rng: Random)
    override fun select(converter: MainDurationAlternative): Duration {
        return context(converter) {
            choiceModel.select().select(rng.nextDouble())
        }
    }

    context(rng: Random)
    override fun selectSingular(converter: MainDurationAlternative): ArrayHistogram {
        return context(converter) {
            choiceModel.select()
        }
    }
    context(converter: MainDurationAlternative)
    override fun probabilitiesSingular() = choiceModel.probabilities()


    private val choiceModelSize by lazy {
        choiceModel.choices.size
    }

    context(rng: Random)
    override fun selectHistogram(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram? {
        if(histograms.all {it.end >= bounds.start && it.start <= bounds.endInclusive  }) {
            return selectAll(converter)
        } else if (histograms.none{it.end >= bounds.start && it.start <= bounds.endInclusive}) {
            return null
        } else {
            val options = histograms.filter { it.end >= bounds.start && it.start <= bounds.endInclusive }.toSet()
            return selectPrecise(converter, options)
        }
    }
    context(rng: Random)
    private fun selectPrecise(converter: MainDurationAlternative, options: Set<ArrayHistogram>): ArrayHistogram? {
        return context(converter) {
            choiceModel.select(options)
        }
    }

    context(rng: Random)
    private fun selectAll(converter: MainDurationAlternative): ArrayHistogram? {
        return context(converter) {
            choiceModel.select()
        }
    }

    context(rng: Random)
    override fun select(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): Duration {

        val concreteHistogram = selectHistogram(bounds, converter) ?: return emergencyBehaviour(bounds, rng)
        val output = concreteHistogram.select(
            bounds
        )
        return output
    }

    /**
     * The legacy code defaces,modifies and otherwise corrupts the histograms for each person individually. Thus, we need a taintable copy.
     */
    override fun taint(): TaintedActivityDurationHistograms {
        return TaintedActivityDurationHistograms(this)
    }

}

interface TaintedHistogramSelector: HistogramSelector {
    fun reset()
    context(rng: Random)
    fun selectAndTaint(
        bounds: ClosedRange<Duration>,
        duration: Duration,
        converter: MainDurationAlternative,
    ): Duration

}
// Every time this class is created, the histograms are copied for tainting. This may be too much copying.
class TaintedActivityDurationHistograms constructor(
    val original: ActivityDurationHistograms,
): TaintedHistogramSelector, HistogramSelector by original {
    val histograms = original.histograms
    val choiceModel = original.choiceModel
    internal val taintedHistograms = histograms.associateWith { CorruptorFactory.originalCorruption(it) }

    override fun reset()  {
        taintedHistograms.forEach { (original, corruption) ->
            corruption.cleanseByLoading(original)
        }
    }


    context(rng: Random)
    override fun selectAndTaint(
        bounds: ClosedRange<Duration>,
        duration: Duration,
        converter: MainDurationAlternative,
    ): Duration {

        val selectedHistogram = original.chooseHistogramFromNeighbors(duration, bounds, converter)
        val taint = taintedHistograms.getValue(selectedHistogram)


        return taint.select(bounds).also {
            taint.modify(it.inWholeMinutes.toInt())
        }
    }
    context(rng: Random)
    override fun select(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): Duration {

        val histogram =
            original.selectHistogram( bounds, converter) ?: return original.emergencyBehaviour(bounds, rng)
        val taint = taintedHistograms.getValue(histogram)
        return taint.select( bounds)
    }
}

enum class Identifier(val id: String, val resourceName: String) {

    WORK_TIME_BUDGETS("7B", "workBudgetHistograms"),
    EDUCATION_TIME_BUDGETS("7D", "educationBudgetHistograms"),
    LEISURE_TIME_BUDGETS("7F", "leisureBudgetHistograms"),
    SHOPPING_TIME_BUDGETS("7H", "shoppingBudgetHistograms"),
    TRANSPORT_TIME_BUDGETS("7J", "transportBudgetHistograms"),

    LEAD_ACTIVITY_DURATION("8C", "leadActivityDurationHistograms"),
    MAJOR_ACTIVITY_DURATION("8E", "majorActivityDurationHistograms"),
    MINOR_ACTIVITY_DURATION("8K", "minorActivityDurationHistograms"),

    FIRST_TOUR_START_TIME("10N", "firstTourStartHistograms"),
    SECOND_TOUR_START_TIME("10P", "secondTourStartHistograms"),
    OTHER_TOUR_START_TIME("10T", "otherTourStartHistograms");

    fun resourcePath(): String {
        return resourceName
    }
}

fun <P : List<T>, T, C> P.generateHistogram(
    histograms: List<ArrayHistogram>,
    characteristicConverter: (MainDurationAlternative) -> C,
    utilityFunction: T.(C) -> Double,
): ActivityDurationHistograms {

    val choiceModel = DiscreteStructure<ArrayHistogram, MainDurationAlternative, P> {
        loadFromList(histograms) { _, it ->
            utilityFunction(this, characteristicConverter(it))
        }
    }.multinomialLogit("Histogram selection Choice model").build(this)

    return ActivityDurationHistograms(histograms, choiceModel)

}


open class PlanAlternative<P : Any>(
    input: MobilityPlanInputs,

    )  {

    val mobilityPlan: MobilityPlan = input.mobilityPlan
    val dayPlan: DayPlan = input.dayPlan
    val tourPlan: TourPlan = input.tourPlan
    val activity: Activity = input.activity
    val planTimeBudgets: TimeBudgets = input.mobilityPlan.timeBudgets
    val person: Person = input.person
    val isLastTourOfDay: Boolean = input.isLastTourOfDay
    val activityType: ActivityType = activity.activityType
    val tourActivity: ActivityType = tourPlan.mainActivity.activityType
    val dayOfWeek: DayOfWeek = dayPlan.durationDay.weekday

    val anztagemit_tourennachht by lazy {
        mobilityPlan.dayPlans.count { it.tourPlans.last().position == Position.AFTER }
    }

    fun householdHasYouths() = person.household.amountOfAllMinors() > 0

    val  anztagemit_tourenvorht by lazy {
        mobilityPlan.dayPlans.count { it.tourPlans.first().position == Position.BEFORE }
    }

    fun anztourenamtag(): Int {
        return dayPlan.tourPlans.size
    }

    fun tour3destages() = tourPlan == dayPlan.tourPlans.getOrNull(2)
    fun isStudent() = person.employment.isStudent()
    fun isAged10to17() = person.age in 10..17
    fun touristhaupttour() = tourPlan.position == Position.MAIN
    fun dauer_hauptakt_tag_4bis6std(): Boolean {
        return dayPlan.durationOfMainActivities in 4.hours..<6.hours
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

    fun tourhat2akt(): Boolean {
        return tourPlan.size == 2
    }

    val tag_anzahl_touren: Int = dayPlan.tourPlans.size

    val activityPositionToMainActivity = activity.position

    val tourPositionToMainTour = tourPlan.position

    fun dauer_akt_vorht_tag_1bis120(): Boolean {
        return dayPlan.tourPlans.filter { it.position == Position.BEFORE }
            .sumOf { it.activityDurationsWithTrips } in 1.minutes..<120.minutes
    }

    fun anztourenvorhaupttour() = dayPlan.tourPlans.count { it.position == Position.BEFORE }
    fun anztourennachhaupttour() = dayPlan.tourPlans.count { it.position == Position.AFTER }

    /**
     * The tour plans activity durations encoded into two hour blocks.
     *  ```
     *  0 -> 0h..<2h
     *  1 -> 2h..<4h
     *  2 -> 4h..<6h
     *  ...
     *  ```
     */
    val dauer_akt_in_tour_blocked: Int get() = tourPlan.activityDurations.inWholeHours.toInt() / 2

    private val anzaktField by lazy {mobilityPlan.regularActivities[activityType] ?: false}
    fun anzaktwieanztagemitzweck(): Boolean {
        return anzaktField
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

    fun letztetourdestages(): Boolean {
        return isLastTourOfDay
    }

    val dayPlanAmountOfActivities: Int = dayPlan.amountOfActivities
}

class MainDurationAlternative(
    input: MobilityPlanInputs,
) : PlanAlternative<ArrayHistogram>(
    input
)



open class BooleanDecisionAlternative(
    input: MobilityPlanInputs,
) : PlanAlternative<Boolean>(
    input
)

class BooleanDecisionWithPreferenceCategory(

    input: MobilityPlanInputs,
    private val preferredHistogram: ArrayHistogram,
) : BooleanDecisionAlternative(
    input
) {
    fun std_start_T1_6_7_Uhr(): Boolean {
        return preferredHistogram.start in (6.hours - 15.minutes)..(6.hours + 15.minutes) // SOme buffer because the histograms could theroretically not have an entry precisely for 6 hours
    }

    fun std_start_T1_7_8_Uhr(): Boolean {
        return preferredHistogram.start in (7.hours - 15.minutes)..(7.hours + 15.minutes) // SOme buffer
    }
}

