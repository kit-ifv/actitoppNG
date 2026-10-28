package edu.kit.ifv.mobitopp.actitoppNG.steps

import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.WeekRoutine

/**
 * An interface, one that gets recycled via delegation by a lot of classes, but the only implementation that does
 * actually implement is [RoutineAttributesFromElement]
 */
interface RoutineAttributes {
    val amountOfWorkingDays: Int
    val amountOfLeisureDays: Int
    val amountOfEducationDays: Int
    val amountOfShoppingDays: Int
    val amountOfServiceDays: Int
    val amountOfImmobileDays: Int
    fun has5WorkDays(): Boolean
    fun has5EducationDays(): Boolean

    val averageAmountOfTours: Int

    val averageAmountOfActivities: Int
}

class RoutineAttributesFromElement(val element: WeekRoutine) : RoutineAttributes {
    override val amountOfWorkingDays = element.amountOfWorkingDays
    override val amountOfLeisureDays = element.amountOfLeisureDays
    override val amountOfEducationDays = element.amountOfEducationDays
    override val amountOfShoppingDays = element.amountOfShoppingDays
    override val amountOfServiceDays = element.amountOfServiceDays
    override val amountOfImmobileDays = element.amountOfImmobileDays
    override fun has5WorkDays() = element.amountOfWorkingDays == 5
    override fun has5EducationDays() = element.amountOfEducationDays == 5
    override val averageAmountOfTours: Int = element.averageAmountOfTours
    override val averageAmountOfActivities: Int = element.averageAmountOfActivities
}