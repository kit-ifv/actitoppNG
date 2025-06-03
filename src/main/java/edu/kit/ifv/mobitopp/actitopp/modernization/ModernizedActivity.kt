package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.DetermineTripDuration
import edu.kit.ifv.mobitopp.actitopp.steps.step2.PersonWithRoutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface Action {
    val startTime: Duration?
    val duration: Duration?
    val endTime: Duration?

    fun shortString(): String
}

interface MutableAction {
    var startTime: Duration?
}
interface MutableLinkedAction: MutableAction, LinkedAction {
    override val previous: MutableLinkedAction?
    override val next: MutableLinkedAction?
}
interface LinkedAction : Action {
    val previous: LinkedAction?
    val next: LinkedAction?

    fun estimatedDuration(defaultDuration: Map<ActivityType, Duration>): Duration
}

interface Activity : Action {
    override val startTime: Duration?
    override val duration: Duration?
    override val endTime: Duration?
    val activityType: ActivityType
    val position: Position


    override fun shortString(): String {
        return "${activityType.typeasChar}[${startTime?.inWholeMinutes?:"?"}, ${endTime?.inWholeMinutes?:"?"}] (${duration?.inWholeMinutes})"
    }
}

/**
 *
 */
interface MutableActivity : Activity, MutableAction {
    override var startTime: Duration?
    override var duration: Duration?
}

operator fun Duration.plus(nullable: Duration?): Duration {
    return nullable?.let { it + this } ?: this
}

class ModernizedActivity(
    override val activityType: ActivityType,
    override var startTime: Duration? = null,
    override var duration: Duration? = null,
    override val position: Position
) : MutableActivity {


    override val endTime get() = startTime?.let { it + duration }



}

class LinkedActivity(
    val original: ModernizedActivity,
    var previousTrip: ModernizedTrip? = null,
    var nextTrip: ModernizedTrip? = null,
) : MutableActivity by original, MutableLinkedAction {


    override val previous: ModernizedTrip?
        get() = previousTrip
    override val next: ModernizedTrip?
        get() = nextTrip

    /**
     * If the duration is not yet set, estimate the duration based on the amount of occurences of a given
     * activity during a day.
     */
    override fun estimatedDuration(defaultDuration: Map<ActivityType, Duration>): Duration {
        return duration ?: defaultDuration.getValue(activityType)
    }

    fun link(other: LinkedActivity, duration: Duration = 15.minutes) {
        val trip = ModernizedTrip(
            duration = duration,
            previousActivity = this,
            nextActivity = other
        )

        this.nextTrip = trip
        other.previousTrip = trip
    }


    fun iterator(): Sequence<LinkedAction> {
        return LinkedActionIterator(this).asSequence()
    }
    fun mutableIterator(): Sequence<MutableLinkedAction> {
        return MutableLinkedActionIterator(this).asSequence()
    }
    fun activityIterator(): Sequence<LinkedActivity> {
        return LinkedActivityIterator(this).asSequence()
    }

    fun backwardIterator(): Sequence<LinkedAction> {
        return BackwardLinkedActionIterator(this).asSequence()
    }

    override fun toString(): String {
        return "$activityType start=($startTime) duration=($duration)"
    }

    companion object {
        fun homeDay(): LinkedActivity = LinkedActivity(ModernizedActivity(ActivityType.HOME, position = Position.MAIN))
    }
}

class MutableLinkedActionIterator(start: MutableLinkedAction) : Iterator<MutableLinkedAction> {
    private var current: MutableLinkedAction? = start
    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): MutableLinkedAction {
        return current!!.also { current = it.next }
    }
}

class LinkedActivityIterator(start: LinkedActivity): Iterator<LinkedActivity> {
    private var current: LinkedActivity? = start
    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): LinkedActivity {
        return current!!.also { current = it.next?.next }
    }
}

class LinkedActionIterator(start: LinkedAction) : Iterator<LinkedAction> {
    private var current: LinkedAction? = start
    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): LinkedAction {
        return current!!.also { current = it.next }
    }
}

class BackwardLinkedActionIterator(start: LinkedAction) : Iterator<LinkedAction> {
    private var current: LinkedAction? = start
    override fun hasNext(): Boolean {
        return current != null
    }

    override fun next(): LinkedAction {
        return current!!.also { current = it.previous }
    }
}

fun List<LinkedActivity>.linkByHomeActivity(
    other: Collection<LinkedActivity>,
    person: PersonWithRoutine,
    tripDuration: DetermineTripDuration,
): List<LinkedActivity> {
    val homeActivity = LinkedActivity.homeDay()
    homeActivity.duration = 1.minutes // Scale home activities to their actual times later, r.n. its 1 minute
    val lastElement = this.last()
    val nextElement = other.first()
    lastElement.link(
        homeActivity, tripDuration.lastTourTrip(
            person = person,
            activityType = lastElement.activityType
        )
    )
    homeActivity.link(nextElement, tripDuration.firstTourTrip(person, nextElement.activityType))
    return this.toMutableList().also { it.add(homeActivity) }
}

class ModernizedTrip(
    override val duration: Duration,
    val previousActivity: LinkedActivity,
    val nextActivity: LinkedActivity,
) : MutableLinkedAction {
    init {

        require(!(previousActivity.activityType == ActivityType.HOME && nextActivity.activityType == ActivityType.HOME)) {
         " This is bad"
        }
    }

    override fun estimatedDuration(defaultDuration: Map<ActivityType, Duration>): Duration {
        return duration
    }

    override var startTime: Duration? = null
    override val endTime: Duration? get() = startTime?.let { it + duration }
    override val previous: LinkedActivity
        get() = previousActivity
    override val next: LinkedActivity
        get() = nextActivity

    override fun toString(): String {
        return "Trip ($duration) ${previousActivity.activityType} (#${
            previousActivity.hashCode().toString().substring(0, 3)
        }) ${nextActivity.activityType} (#${nextActivity.hashCode().toString().substring(0, 3)})"
    }

    override fun shortString(): String {
        return " -(${duration.inWholeMinutes})->"
    }
}
