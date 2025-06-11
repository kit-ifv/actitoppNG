package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.HomeDayPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MovingDayPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MovingDayPlanInput
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MutableDayPlan
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalQueue

import java.time.DayOfWeek
import kotlin.time.Duration

// TODO seal interface once HDay is killed
/**
 * The day structure contains the current tour structures that will be present on a given day, this is a readonly view.
 */
interface DayStructure :Comparable<DayStructure> {
    val startTimeDay: DurationDay
    val weekday: DayOfWeek
    val duration : Duration

    override fun compareTo(other: DayStructure): Int {
        return startTimeDay.compareTo(other.startTimeDay)
    }

    fun mainActivityType(): ActivityType
    fun amountOfPrecursorElements(): Int
    fun amountOfSuccessorElements(): Int
    fun amountOfElements(): Int
    // TODO elements could probably be an iterator
    fun elements(): Collection<MutableTourStructure>
    fun indexedElements(): Collection<BidirectionalIndexedValue<MutableTourStructure>>
    fun amountOfActivities() = elements().sumOf { it.size }
    fun getPlannedTourAmounts():PlannedTourAmounts = PlannedTourAmounts(amountOfPrecursorElements(), amountOfSuccessorElements())

    operator fun contains(activityType: ActivityType): Boolean {
        return elements().any { activityType in it }
    }
    val minimumAmountOfToursByJointActions: Int get() {throw UnsupportedOperationException("Nope")}
    val minimumAmountOfActivitiesByJointActions: Int get() {throw UnsupportedOperationException("Nope")}



    fun toDayPlan(movingDayPlanInput: MovingDayPlanInput): MutableDayPlan
}

class HomeDay private constructor(
    val tourStructureCollection: Collection<TourStructure>,
    override val startTimeDay: DurationDay) : DayStructure {

    constructor(startTimeDay: DurationDay): this(emptyList(), startTimeDay)
    override val weekday: DayOfWeek = startTimeDay.weekday
    override val duration: Duration = startTimeDay.startOfDay
    override fun mainActivityType(): ActivityType = ActivityType.HOME

    override fun amountOfPrecursorElements(): Int = 0

    override fun amountOfSuccessorElements(): Int = 0
    override fun amountOfElements(): Int = 0
    override fun getPlannedTourAmounts(): PlannedTourAmounts = PlannedTourAmounts.NONE
    override val minimumAmountOfToursByJointActions: Int = 0
    override fun elements(): Collection<MutableTourStructure> = emptySet()
    override fun indexedElements(): Collection<BidirectionalIndexedValue<MutableTourStructure>> = emptySet()

    override fun toDayPlan(movingDayPlanInput: MovingDayPlanInput): MutableDayPlan {
        return HomeDayPlan(startTimeDay)
    }
}

class ModifiableDayStructure(override val startTimeDay: DurationDay, mainTourStructure: MutableTourStructure) :
    BidirectionalQueue<MutableTourStructure>(mainTourStructure), DayStructure {
    constructor(dayIndex: Int, mainTourStructure: MutableTourStructure) : this(DurationDay(dayIndex), mainTourStructure)

    override val weekday: DayOfWeek = startTimeDay.weekday
    override val duration = startTimeDay.startOfDay
    // TODO maybe protect this field from modification, right now it is just a template holder for 3A
    override var minimumAmountOfToursByJointActions: Int = 0
    // TODO, joint action modelling should not be interweaved with normal structures.
    override val minimumAmountOfActivitiesByJointActions: Int = 0
    fun loadPrecursors(activityTypes: Collection<ActivityType>) {
        activityTypes.reversed().forEach {
            addPrecursor(MutableTourStructure(it))
        }
    }

    fun loadSuccessors(activityTypes: Collection<ActivityType>) {
        activityTypes.forEach {
            addSuccessor(MutableTourStructure(it))
        }
    }

    override fun mainActivityType(): ActivityType = mainTourActivityType()
    private fun mainTourActivityType(): ActivityType {
        return this[0][0]
    }

    override fun toString(): String {
        return "Week (${duration.inWholeDays / 7}) Main Act: [${mainTourActivityType()}] ${
            weekday.toString().substring(0, 3)
        } Planned Tours: (${elements().joinToString()})"
    }

    override fun toDayPlan(movingDayPlanInput: MovingDayPlanInput): MutableDayPlan {
        return MovingDayPlan.create(
            indexedElements(),
            movingDayPlanInput
        )
    }
}




