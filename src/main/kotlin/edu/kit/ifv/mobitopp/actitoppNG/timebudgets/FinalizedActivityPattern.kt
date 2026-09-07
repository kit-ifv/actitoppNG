package edu.kit.ifv.mobitopp.actitoppNG.timebudgets

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure


/**
 * The activity synthesis is done, the tours are fixed, and so are the activities. They do not change anymore.
 *
 * this could be used in the daily time activity calculation. TODO this is now the MobilityPlan, which does not use
 * the dirty H(X) Fields.
 */

interface FinalizedActivityPattern {
    val workDays: Int
    val educationDays: Int
    val leisureDays: Int
    val shoppingDays: Int
    val transportDays: Int
    val workActivities: Int
    val educationActivities: Int
    val leisureActivities: Int
    val shoppingActivities: Int
    val transportActivities: Int

    companion object {

        fun fromModernPattern(mobilityStructure: MobilityStructure): FinalizedActivityPatternImpl {
            val activityMap = mobilityStructure.activityTypes().groupBy { it }.mapValues { it.value.size }
            return FinalizedActivityPatternImpl(
                workDays = mobilityStructure.amountOfDaysWith(ActivityType.WORK),
                educationDays = mobilityStructure.amountOfDaysWith(ActivityType.EDUCATION),
                leisureDays = mobilityStructure.amountOfDaysWith(ActivityType.LEISURE),
                shoppingDays = mobilityStructure.amountOfDaysWith(ActivityType.SHOPPING),
                transportDays = mobilityStructure.amountOfDaysWith(ActivityType.TRANSPORT),
                workActivities = activityMap[ActivityType.WORK] ?: 0,
                educationActivities = activityMap[ActivityType.EDUCATION] ?: 0,
                leisureActivities = activityMap[ActivityType.LEISURE] ?: 0,
                shoppingActivities = activityMap[ActivityType.SHOPPING] ?: 0,
                transportActivities = activityMap[ActivityType.TRANSPORT] ?: 0,
            )
        }
    }
}

data class FinalizedActivityPatternImpl(
    override val workDays: Int,
    override val educationDays: Int,
    override val leisureDays: Int,
    override val shoppingDays: Int,
    override val transportDays: Int,
    override val workActivities: Int,
    override val educationActivities: Int,
    override val leisureActivities: Int,
    override val shoppingActivities: Int,
    override val transportActivities: Int,
) : FinalizedActivityPattern


interface FinalizedPatternAttributes {
    fun amountOfWorkActivitiesInWeek(): Int
    fun amountOfEducationActivitiesInWeek(): Int
    fun amountOfLeisureActivitiesInWeek(): Int
    fun amountOfShoppingActivitiesInWeek(): Int
    fun amountOfTransportActivitiesInWeek(): Int

    val amountOfDaysWithWorkActivity: Int

    val amountOfDaysWithLeisureActivity: Int

    val amountOfDaysWithEducationActivity: Int

    val amountOfDaysWithShoppingActivity: Int

    fun amountOfDaysWithTransportActivityIs1(): Boolean
}

class PatternAttributesByElement(val element: FinalizedActivityPattern) : FinalizedPatternAttributes {
    override fun amountOfWorkActivitiesInWeek(): Int {
        return element.workActivities
    }

    override fun amountOfEducationActivitiesInWeek(): Int {
        return element.educationActivities
    }

    override fun amountOfLeisureActivitiesInWeek(): Int = element.leisureActivities
    override fun amountOfShoppingActivitiesInWeek(): Int = element.shoppingActivities
    override fun amountOfTransportActivitiesInWeek(): Int = element.transportActivities

    override val amountOfDaysWithWorkActivity: Int
        get() = element.workDays

    override val amountOfDaysWithEducationActivity: Int
        get() = element.educationDays

    override val amountOfDaysWithLeisureActivity: Int
        get() = element.leisureDays

    override val amountOfDaysWithShoppingActivity: Int
        get() = element.shoppingDays

    override fun amountOfDaysWithTransportActivityIs1() = element.transportDays == 1
}