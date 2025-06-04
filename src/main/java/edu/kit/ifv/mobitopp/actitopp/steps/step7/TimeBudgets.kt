package edu.kit.ifv.mobitopp.actitopp.steps.step7

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.changes.Category
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

// TODO mention somewhere that the categories are indexed starting from 1 isntead of 0, and eventually change the index t 0based.
data class TimeBudgets(

    val workBudget: Duration,
    val educationBudget: Duration,
    val leisureBudget: Duration,
    val shoppingBudget: Duration,
    val transportBudget: Duration,

    val workCategory: Category,
    val educationCategory: Category,
    val leisureCategory: Category,
    val shoppingCategory: Category,
    val transportCategory: Category,
) {
    fun toDayTimeBudget(dailyOccurrences: Map<ActivityType, Int>): TimeBudgets {
        return TimeBudgets(
            workBudget = safeDivide(workBudget ,dailyOccurrences.getOrDefault(ActivityType.WORK, 0)),
            educationBudget = safeDivide(educationBudget ,dailyOccurrences.getOrDefault(ActivityType.EDUCATION, 0)),
            leisureBudget = safeDivide(leisureBudget ,dailyOccurrences.getOrDefault(ActivityType.LEISURE, 0)),
            shoppingBudget = safeDivide(shoppingBudget ,dailyOccurrences.getOrDefault(ActivityType.SHOPPING, 0)),
            transportBudget = safeDivide(transportBudget ,dailyOccurrences.getOrDefault(ActivityType.TRANSPORT, 0)),
            workCategory = workCategory,
            educationCategory = educationCategory,
            leisureCategory = leisureCategory,
            shoppingCategory = shoppingCategory,
            transportCategory = transportCategory,
        )
    }
    fun safeDivide(numerator: Duration, denominator: Int) : Duration {
        if(numerator == Duration.ZERO) return Duration.ZERO
        if(denominator == 0) return Duration.INFINITE
        return numerator / denominator
    }
    operator fun get(activityType: ActivityType): Duration {
        return when (activityType) {
            ActivityType.WORK -> workBudget
            ActivityType.EDUCATION -> educationBudget
            ActivityType.LEISURE -> leisureBudget
            ActivityType.SHOPPING -> shoppingBudget
            ActivityType.TRANSPORT -> transportBudget
            ActivityType.HOME -> 0.minutes
            else -> throw NoSuchElementException("activityType $activityType not supported")
        }
    }

    fun getCategory(activityType: ActivityType): Category {
        return when (activityType) {
            ActivityType.WORK -> workCategory
            ActivityType.EDUCATION -> educationCategory
            ActivityType.LEISURE -> leisureCategory
            ActivityType.SHOPPING -> shoppingCategory
            ActivityType.TRANSPORT -> transportCategory

            else -> throw NoSuchElementException("activityType $activityType not supported")

        }
    }
}

val NO_TIME: Pair<Duration, Category> = Duration.ZERO to Category(0)
class HistogramPerActivity(
    val workHistograms: WorkHistograms = WorkHistograms.fromResourcePath(),
    val educationHistograms: EducationHistograms = EducationHistograms.fromResourcePath(),
    val leisureHistograms: LeisureHistograms = LeisureHistograms.fromResourcePath(),
    val shoppingHistograms: ShoppingHistograms = ShoppingHistograms.fromResourcePath(),
    val transportHistograms: TransportHistograms = TransportHistograms.fromResourcePath(),
) {


    fun determineTimeBudgets(
        rngKeeper: RNGKeeper,
        finalizedActivityPattern: FinalizedActivityPattern,
    ): TimeBudgets {
        val workSelection = if (finalizedActivityPattern.workDays.isEmpty()) NO_TIME else
            workHistograms.select(rngKeeper.pull("7A"),
            rngKeeper.pull("7B"), finalizedActivityPattern)
        val educationSelection = if (finalizedActivityPattern.educationDays.isEmpty()) NO_TIME else
        educationHistograms.select(rngKeeper.pull("7C"), rngKeeper.pull("7D"), finalizedActivityPattern)
        val leisureSelection = if (finalizedActivityPattern.leisureDays.isEmpty()) NO_TIME else
        leisureHistograms.select(rngKeeper.pull("7E"), rngKeeper.pull("7F"), finalizedActivityPattern)
        val shoppingSelection = if (finalizedActivityPattern.shoppingDays.isEmpty()) NO_TIME else
        shoppingHistograms.select(rngKeeper.pull("7G"), rngKeeper.pull("7H"), finalizedActivityPattern)
        val transportSelection = if (finalizedActivityPattern.transportDays.isEmpty()) NO_TIME else
        transportHistograms.select(rngKeeper.pull("7I"), rngKeeper.pull("7J"), finalizedActivityPattern)
        return TimeBudgets(
            workBudget = workSelection.first,
            workCategory = workSelection.second,
            educationBudget = educationSelection.first,
            leisureBudget = leisureSelection.first,
            shoppingBudget = shoppingSelection.first,
            transportBudget = transportSelection.first,
            educationCategory = educationSelection.second,
            leisureCategory = leisureSelection.second,
            shoppingCategory = shoppingSelection.second,
            transportCategory = transportSelection.second,
        )
    }

    private fun HistogramSelection.select(
        rngHelper: RNGHelper,
        finalizedActivityPattern: FinalizedActivityPattern,
    ): Pair<Duration, Category> {
        val histogram = select(rngHelper.randomValue, finalizedActivityPattern)
        return histogram.selectInt(rngHelper.randomValue) to histogram.categoryIndex
    }

    private fun HistogramSelection.select(
        firstRnd: Double,
        secondRnd: Double,
        finalizedActivityPattern: FinalizedActivityPattern,
    ): Pair<Duration, Category> {
        val histogram = select(firstRnd, finalizedActivityPattern)
        return histogram.selectInt(secondRnd) to histogram.categoryIndex
    }
}
