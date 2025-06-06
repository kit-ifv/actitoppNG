package edu.kit.ifv.mobitopp.actitopp.modernization.plan

import edu.kit.ifv.mobitopp.actitopp.Coordinator
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.Activity
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.DurationDay
import edu.kit.ifv.mobitopp.actitopp.modernization.LinkedAction
import edu.kit.ifv.mobitopp.actitopp.modernization.LinkedActivity
import edu.kit.ifv.mobitopp.actitopp.modernization.linkByHomeActivity
import edu.kit.ifv.mobitopp.actitopp.steps.step2.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.steps.step7.TimeBudgets
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class MobilityPlan(
    val dayPlans: Collection<MutableDayPlan>,
    val homePlans: Collection<HomeDayPlan>,
    val activities: Collection<LinkedActivity>,
    val timeBudgets: TimeBudgets,
    val person: IPerson,
    tripDuration: DetermineTripDuration = StandardCommuteDurations(),
) {
    // Assume that the agent starts their plan at home.
    private val startHomeAnchor = LinkedActivity.homeDay().apply {
        startTime = 0.minutes
        duration = 1.minutes
    }

    // And ends their mobility pattern at home.
    private val endHomeAnchor = LinkedActivity.homeDay().apply {
        startTime = (dayPlans.size + homePlans.size).days - 1.minutes + 3.hours
        duration = 1.minutes
    }

    init {
        startHomeAnchor.link(
            activities.first(),
            duration = tripDuration.firstTourTrip(person, activities.first().activityType)
        )
        activities.last()
            .link(endHomeAnchor, duration = tripDuration.lastTourTrip(person, activities.last().activityType))
    }

    fun amountOfDaysWithActivity(activityType: ActivityType): Int {
        TODO()
    }
    fun outOfHomeActivities() = activities.filter { it.activityType != ActivityType.HOME }
    val activityMap: Map<ActivityType, List<LinkedActivity>> = activities.groupBy { it.activityType }
    val mainActivityMap : Map<ActivityType, List<LinkedActivity>> = dayPlans.flatMap { it.tourPlans.map { it.mainActivity } }.groupBy{it.activityType}

    /**
     * An activity is regular, if the amount of activities per week is equal to the number of days with said activity
     */
    val regularActivities: Map<ActivityType, Boolean> by lazy {
        val dayMap = ActivityType.OUTOFHOMEACTIVITY.associateWith { actType ->
            dayPlans.count { it.hasActivity(actType) }
        }
        activities.groupBy { it.activityType }.mapValues { (key, value) ->
            value.size == dayMap[key]
        }
    }


    fun isConsistent(): Boolean {
        return startHomeAnchor.iterator().all {
            it.isConsistent()
        }
    }


    fun filterInconsistent(): List<LinkedAction> {
        return startHomeAnchor.iterator().filter { !it.isConsistent() }.toList()
    }

    fun fullPrint() {
        println(startHomeAnchor.iterator().joinToString(separator = "\n") { it.shortString() })
    }
    fun extrudeHomeActivities() {
        startHomeAnchor.startTime  = Duration.ZERO
        startHomeAnchor.activityIterator().filter { it.activityType == ActivityType.HOME }.forEach { act ->
            val previousAct = act.previous?.previousActivity
            previousAct?.let {

                val duration = it.endTime!! + act.previous!!.duration
                act.startTime = duration

            }

            val nextAct = act.next?.nextActivity
            nextAct?.let {
                val duration = it.startTime!! - act.next!!.duration - act.startTime!!
                require(duration >= Duration.ZERO) {
                    "The duration should never be negative, some other calculation fucked up"
                }
                act.duration = duration
            }
        }
        endHomeAnchor.duration = dayPlans.size.days - endHomeAnchor.startTime!!
    }

    override fun toString(): String {
        return startHomeAnchor.activityIterator().joinToString(separator = "\n") { it.shortString() }
    }



    companion object {
        fun create(
            dayStructures: Collection<DayStructure>,
            homeDays: Collection<DurationDay>,
            timeBudgets: TimeBudgets,
            personWithRoutine: PersonWithRoutine,
            tripDuration: DetermineTripDuration,
        ): MobilityPlan {
            val counts =
                ActivityType.FULLSET.associateWith { activityType -> dayStructures.filter { day -> activityType in day }.size }
            val dayTimeBudgets = timeBudgets.toDayTimeBudget(counts)

            val dayPlans = dayStructures.map {
                it.toDayPlan(
                    MovingDayPlanInput(
                        personWithRoutine = personWithRoutine,
                        tripDuration = tripDuration,
                        timeBudgets = dayTimeBudgets,
                        durationDay = it.startTimeDay
                    )
                )
            }


            val homePlans = homeDays.map {
                HomeDayPlan(it)
            }

            val activities = dayPlans.zipWithNext().flatMap { (firstDay, secondDay) ->
                firstDay.linkByHomeActivity(secondDay, personWithRoutine, tripDuration)
            } + dayPlans.last()
            return MobilityPlan(
                dayPlans, homePlans, activities, timeBudgets, personWithRoutine
            )
        }
    }

}

