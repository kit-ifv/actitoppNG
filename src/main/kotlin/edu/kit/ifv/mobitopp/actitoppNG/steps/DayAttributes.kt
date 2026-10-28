package edu.kit.ifv.mobitopp.actitoppNG.steps


import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DayStructure
import java.time.DayOfWeek


/**
 * This is a combinatorical interface to encapsulate the different utility function components used in actitopp
 * Originally this interface existed to replicate the original structure and replace it with a more sensible structure
 * which is essentially just [DayOfWeek]. During the rewrite I really needed this interface to get some sensible structure
 * into actitopp. Now it may be no longer needed.
 */
interface DayAttributes {
    val isStandardWorkingDay: Boolean

    // Introduced for performant lookups and not stepping through the interface.
    val dayOfWeek: DayOfWeek
}

interface DayStructureAttributes : DayAttributes {
    val dayMainActivity: ActivityType
}

/**
 * When the day is completely structured, we can determine the amount of tours.
 */
interface FullyQualifiedDayStructureAttributes : DayStructureAttributes {
    fun amountOfTours(): Int
}

fun interface PartialTourLayoutAttributes {
    fun amountOfBeforeTours(): Int
}


class DayAttributesFromStructure(private val element: DayStructure) : PartialTourLayoutAttributes,
    DayStructureAttributes, FullyQualifiedDayStructureAttributes {

    override val dayOfWeek: DayOfWeek = element.weekday

    override val isStandardWorkingDay: Boolean = element.weekday in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
    override val dayMainActivity: ActivityType = element.mainActivityType()

    override fun amountOfBeforeTours(): Int = element.amountOfPrecursorElements()
    override fun amountOfTours() = element.amountOfElements()
}

class DayAttributesFromWeekday(element: DayOfWeek) : DayAttributes {
    override val isStandardWorkingDay: Boolean = element in DayOfWeek.MONDAY..DayOfWeek.FRIDAY
    override val dayOfWeek: DayOfWeek = element
}