package edu.kit.ifv.mobitopp.actitoppNG.weekroutine

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

/**
 * This class provides a mutable state of a [WeekRoutine]. The fields are deliberately set to Delegates.notNull() instead
 * of = 0. This way preemptive access of uninitialized fields causes an error, rather than a silent calculation with 0.
 * (We assume that this silent calculation mismatch is not desired, and thus guarded with errors)
 */
@Serializable
class ModifiableWeekRoutine constructor() : WeekRoutine {
    override var amountOfWorkingDays: Int = -1
    override var amountOfEducationDays: Int = -1
    override var amountOfLeisureDays: Int = -1
    override var amountOfShoppingDays: Int = -1
    override var amountOfServiceDays: Int = -1
    override var amountOfImmobileDays: Int = -1
    override var averageAmountOfTours: Int = -1
    override var averageAmountOfActivities: Int = -1

}



