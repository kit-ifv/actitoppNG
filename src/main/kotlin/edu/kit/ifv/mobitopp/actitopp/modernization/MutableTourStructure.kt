package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.DetermineTripDuration
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.StandardCommuteDurations
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.TourPlan
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalQueue
import edu.kit.ifv.mobitopp.actitopp.utils.Position

interface TourStructure : Collection<ActivityType> {
    fun mainActivityType(): ActivityType
    fun elements(): Collection<ActivityType>

    fun amountOfPrecursorElements(): Int
    fun amountOfSuccessorElements(): Int
    fun amountOfElements(): Int
}


/**
 * A tour structure is only the ordering of activity types taken during a tour, but not fully qualified activities, as
 * none of the duration flags such as start-time or duration are known yet. Later this tour structure should be
 * translated to a fully qualified tour. We try to honor the legacy approach of scaling along the main activity type
 * from index 0 and adding precursor and successor activity types by appending on both sides of the indices.
 *
 * We only generate a tour if there is a known main activity, thus we can require the first activity to be known before
 * instantiation.
 */
class MutableTourStructure(
    mainActivityType: ActivityType,
) : BidirectionalQueue<ActivityType>(mainActivityType), TourStructure {


    fun loadPrecursors(activityTypes: Collection<ActivityType>) {
        activityTypes.reversed().forEach {
            addPrecursor(it)
        }
    }

    fun loadSuccessors(activityTypes: Collection<ActivityType>) {
        activityTypes.forEach {
            addSuccessor(it)
        }
    }

    override fun mainActivityType(): ActivityType {
        return get(0)
    }

    override fun toString(): String {
        return "${precursors().joinToString { it.typeasChar.toString() }} [${mainElement().typeasChar}] ${successors().joinToString { it.typeasChar.toString() }}"
    }

    fun toPlan(
        personWithRoutine: PersonWithRoutine,
        position: Position,
        tripDuration: DetermineTripDuration = StandardCommuteDurations.STANDARD_ASSIGNMENT,
    ): TourPlan {
        return TourPlan.create(this, personWithRoutine, position, tripDuration)
    }

}

