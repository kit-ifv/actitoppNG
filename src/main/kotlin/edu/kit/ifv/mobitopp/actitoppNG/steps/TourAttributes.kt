package edu.kit.ifv.mobitopp.actitoppNG.steps


import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitoppNG.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitoppNG.utils.Position

interface TourPositionAttributes {
    /**
     * 0 indexed so:
     *
     * 0 -> first tour of the day
     *
     * 1 -> second tour of the day
     *
     * 2 -> ...
     */
    val tourIndexOfDay: Int
    val isBeforeMainTour: Boolean
    val isAfterMainTour: Boolean
}

interface ActivityAmountAttributes {
    val numActivitiesBeforeMainActivity: Int
}

class ActivityAmountByNumber(val element: Int) : ActivityAmountAttributes {
    override val numActivitiesBeforeMainActivity: Int
        get() = element
}

interface TourAttributes : TourPositionAttributes {
    val tourMainActivityType: ActivityType

    val numTourActivities: Int
}

class TourPositionAttributesByIndex(val absoluteIndex: Int, val position: Position) : TourPositionAttributes {
    override val tourIndexOfDay: Int
        get() = absoluteIndex

    override val isBeforeMainTour: Boolean = position == Position.BEFORE

    override val isAfterMainTour: Boolean = position == Position.AFTER

}

class TourAttributesByStructAndNumbers(
    private val indexedTour: BidirectionalIndexedValue<TourStructure>,
    val precursorActivityCount: Int,
    val successorActivityCount: Int,

    ) : TourAttributes, TourPositionAttributes, ActivityAmountAttributes {

    override val tourMainActivityType: ActivityType
        get() = indexedTour.element.mainActivityType()

    override val numTourActivities: Int
        get() = indexedTour.element.elements().size + precursorActivityCount + successorActivityCount

    override val tourIndexOfDay: Int
        get() = indexedTour.absoluteIndex

    override val isBeforeMainTour: Boolean = indexedTour.position == Position.BEFORE

    override val isAfterMainTour: Boolean = indexedTour.position == Position.AFTER

    override val numActivitiesBeforeMainActivity: Int
        get() = indexedTour.element.amountOfPrecursorElements() + precursorActivityCount
}

class TourAttributesByIndexedStructure(private val indexedTour: BidirectionalIndexedValue<TourStructure>) :
    TourAttributes, TourPositionAttributes, ActivityAmountAttributes {

    override val tourIndexOfDay: Int
        get() = indexedTour.absoluteIndex

    override val isBeforeMainTour: Boolean = indexedTour.position == Position.BEFORE

    override val isAfterMainTour: Boolean = indexedTour.position == Position.AFTER

    override val tourMainActivityType: ActivityType
        get() = indexedTour.element.mainActivityType()

    override val numActivitiesBeforeMainActivity: Int
        get() = indexedTour.element.amountOfPrecursorElements()

    override val numTourActivities: Int
        get() = indexedTour.element.amountOfElements()
}