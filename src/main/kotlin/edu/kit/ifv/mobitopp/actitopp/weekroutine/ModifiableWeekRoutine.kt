package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import org.jetbrains.annotations.TestOnly
import kotlin.properties.Delegates

class ModifiableWeekRoutine {
    var amountOfWorkingDays: Int by Delegates.notNull()
    var amountOfEducationDays: Int by Delegates.notNull()
    var amountOfLeisureDays: Int by Delegates.notNull()
    var amountOfShoppingDays: Int by Delegates.notNull()
    var amountOfServiceDays: Int by Delegates.notNull()
    var amountOfImmobileDays: Int by Delegates.notNull()
    var averageAmountOfTours: Int by Delegates.notNull()
    var averageAmountOfActivities: Int by Delegates.notNull()

    fun toWeekRoutine(): WeekRoutine {
        return WeekRoutineImpl(
            amountOfWorkingDays = amountOfWorkingDays,
            amountOfEducationDays = amountOfEducationDays,
            amountOfLeisureDays = amountOfLeisureDays,
            amountOfShoppingDays = amountOfShoppingDays,
            amountOfServiceDays = amountOfServiceDays,
            amountOfImmobileDays = amountOfImmobileDays,
            averageAmountOfTours = averageAmountOfTours,
            averageAmountOfActivities = averageAmountOfActivities
        )
    }
}


interface WeekRoutine {
    val amountOfWorkingDays: Int
    val amountOfEducationDays: Int
    val amountOfLeisureDays: Int
    val amountOfShoppingDays: Int
    val amountOfServiceDays: Int
    val amountOfImmobileDays: Int
    val averageAmountOfTours: Int
    val averageAmountOfActivities: Int

    operator fun get(activityType: ActivityType): Int {
        return when (activityType) {
            ActivityType.EDUCATION -> amountOfEducationDays
            ActivityType.WORK -> amountOfWorkingDays
            ActivityType.LEISURE -> amountOfLeisureDays
            ActivityType.SHOPPING -> amountOfShoppingDays
            ActivityType.TRANSPORT -> amountOfServiceDays
            ActivityType.HOME -> amountOfImmobileDays
            else -> throw UnsupportedOperationException("the week routine has no field to track $activityType")
        }
    }
}

data class WeekRoutineImpl(
    override val amountOfWorkingDays: Int,
    override val amountOfEducationDays: Int,
    override val amountOfLeisureDays: Int,
    override val amountOfShoppingDays: Int,
    override val amountOfServiceDays: Int,
    override val amountOfImmobileDays: Int,
    override val averageAmountOfTours: Int,
    override val averageAmountOfActivities: Int,
) : WeekRoutine



