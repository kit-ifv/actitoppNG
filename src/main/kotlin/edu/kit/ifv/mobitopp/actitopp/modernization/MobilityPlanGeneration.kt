package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.STATIC_HISTOGRAMS
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.LegacySpawnSideTours
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.SpawnSideTours
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.SpawnWeek
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.StandardImplementation
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.StandardCommuteDurations
import edu.kit.ifv.mobitopp.actitopp.spawnRandomGenerator

import edu.kit.ifv.mobitopp.actitopp.weekroutine.generateWeekRoutine
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.choicemodels.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.TourStartByHistogramsRelative
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.TourStartWithPreference
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.assignFirstTourStarts
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.assignRemainingTourStarts
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.assignSecondTourStarts
import edu.kit.ifv.mobitopp.actitopp.timebudgets.FinalizedActivityPattern
import edu.kit.ifv.mobitopp.actitopp.plandurations.AssignMinorActivityDuration
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.LEAD
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitopp.plandurations.StandardStep8B
import edu.kit.ifv.mobitopp.actitopp.plandurations.assignFirstMainActivities
import edu.kit.ifv.mobitopp.actitopp.plandurations.assignMinorActivities
import edu.kit.ifv.mobitopp.actitopp.plandurations.assignSecondaryMainActivities
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.StandardPreferredTourStart
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.assignPreferredTourStart


fun interface MobilityPlanGeneration {
    fun generate(person: IPerson, amountOfDays: Int): MobilityPlan
    fun generate(person: IPerson) = generate(person, 7)
}

class DefaultPlanGeneration : MobilityPlanGeneration {
    override fun generate(person: IPerson, amountOfDays: Int): MobilityPlan {
        val rng = person.spawnRandomGenerator()
        val structureGenerator = StandardStructureGeneration(rng)
        val durationGenerator = StandardDurationAssignment(rng)
        val startTimeGenerator = StandardStartTimeAssignment(rng)
        val mobilityPlan = structureGenerator.generate(person, amountOfDays)
        mobilityPlan.apply {
            durationGenerator.assignDurations(this)
            startTimeGenerator.assignStartTimes(this)
        }
        mobilityPlan.extrudeHomeActivities()
        return mobilityPlan
    }
}

class StandardStructureGeneration(val rng: RNGHelper) : MobilityPlanGeneration {
    private val sideActivityStrategy = StandardImplementation(rng)
    private val spawnMainActivities = SpawnWeek(rng)
    private val spawnSideTours: SpawnSideTours = LegacySpawnSideTours(rng)
    override fun generate(person: IPerson, amountOfDays: Int): MobilityPlan {

        val weekRoutine = person.generateWeekRoutine(rng)
        val mobilityStructure = MobilityStructure(person, weekRoutine)
        // Generate the main activities of each day
        spawnMainActivities.spawnMainActivities(mobilityStructure)
        // Determine the amount of tours that precede & succeed the main tour.
        spawnSideTours.spawnSideTours(mobilityStructure)
        sideActivityStrategy.spawnSideActivities(mobilityStructure)
        // Determine the amount of precursor and successor tours for each tour of the day

        val budget = STATIC_HISTOGRAMS.determineTimeBudgets(rng, person, FinalizedActivityPattern.fromModernPattern(mobilityStructure))
        return mobilityStructure.toPlan(StandardCommuteDurations.STANDARD_ASSIGNMENT, budget)?: MobilityPlan.stayAtHomePlan(person, amountOfDays)
    }
}

fun interface MobilityPlanDurationAssignment {
    fun assignDurations(mobilityPlan: MobilityPlan)
}
class  StandardDurationAssignment(val rng: RNGHelper): MobilityPlanDurationAssignment {
    override fun assignDurations(mobilityPlan: MobilityPlan) {
        mobilityPlan.assignFirstMainActivities(StandardStep8B(rng, LEAD,))
        mobilityPlan.assignSecondaryMainActivities(StandardStep8B(rng, MAJOR, ))
        mobilityPlan.assignMinorActivities(AssignMinorActivityDuration(rng, ))

    }
}
fun interface MobilityPlanStartTimeAssignment {
    fun assignStartTimes(mobilityPlan: MobilityPlan)
}

class StandardStartTimeAssignment(val rng: RNGHelper) : MobilityPlanStartTimeAssignment{
    override fun assignStartTimes(mobilityPlan: MobilityPlan) {
        val preferredHistogram = mobilityPlan.assignPreferredTourStart(StandardPreferredTourStart(rng))

        val firstStrategy = TourStartWithPreference(
            rng = rng,
            startTimeHistograms = FIRST_TOUR_HISTOGRAM,
            preferredTourStart = preferredHistogram,
            usePreferredTourStart = true,
        )

        val secondStrategy = TourStartWithPreference(
            rng = rng,
            startTimeHistograms = SECOND_TOUR_HISTOGRAM,
            preferredTourStart = preferredHistogram,
            usePreferredTourStart = false,
        )
        mobilityPlan.assignFirstTourStarts(firstStrategy)
        mobilityPlan.assignSecondTourStarts(secondStrategy)
        mobilityPlan.assignRemainingTourStarts(TourStartByHistogramsRelative.standard(rng))
    }

}
