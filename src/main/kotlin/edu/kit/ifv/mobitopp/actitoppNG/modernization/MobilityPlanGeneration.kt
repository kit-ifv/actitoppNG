package edu.kit.ifv.mobitopp.actitoppNG.modernization


import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.LegacySpawnSideTours
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.SpawnSideTours
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.SpawnWeek
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.StandardImplementation
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.StandardCommuteDurations
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.AssignMinorActivityDuration
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.StickySelector
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.assignFirstMainActivities
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.assignMinorActivities
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.assignSecondaryMainActivities
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.LEAD
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.FinalizedActivityPattern
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramPerActivity
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.PreferredStartViaChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.StandardPreferredTourStart
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.TourStartByHistogramsRelative
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.TourStartWithPreference
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.UsePreferredTourStart
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.assignFirstTourStarts
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.assignPreferredTourStart
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.assignRemainingTourStarts
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.assignSecondTourStarts
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.generateWeekRoutine


fun interface MobilityPlanGeneration {
    fun generate(person: Person, amountOfDays: Int): MobilityPlan
    fun generate(person: Person) = generate(person, 7)
}

class DefaultPlanGeneration : MobilityPlanGeneration {
    override fun generate(person: Person, amountOfDays: Int): MobilityPlan {
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

class StandardStructureGeneration(
    val rng: RNGHelper,
    private val histograms: HistogramPerActivity = HistogramPerActivity.DEFAULT,
) : MobilityPlanGeneration {
    private val sideActivityStrategy = StandardImplementation(rng)
    private val spawnMainActivities = SpawnWeek(rng)
    private val spawnSideTours: SpawnSideTours = LegacySpawnSideTours(rng)
    override fun generate(person: Person, amountOfDays: Int): MobilityPlan {

        val weekRoutine = person.generateWeekRoutine(rng)
        val mobilityStructure = MobilityStructure(person, weekRoutine)
        // Generate the main activities of each day
        spawnMainActivities.spawnMainActivities(mobilityStructure)
        // Determine the amount of tours that precede & succeed the main tour.
        spawnSideTours.spawnSideTours(mobilityStructure)
        sideActivityStrategy.spawnSideActivities(mobilityStructure)
        // Determine the amount of precursor and successor tours for each tour of the day

        val budget = histograms.determineTimeBudgets(
            rng,
            person,
            FinalizedActivityPattern.fromModernPattern(mobilityStructure)
        )
        return mobilityStructure.toPlan(StandardCommuteDurations.STANDARD_ASSIGNMENT, budget)
    }
}

fun interface MobilityPlanDurationAssignment {
    fun assignDurations(mobilityPlan: MobilityPlan)
}

class StandardDurationAssignment(val rng: RNGHelper) : MobilityPlanDurationAssignment {
    override fun assignDurations(mobilityPlan: MobilityPlan) {
        mobilityPlan.assignFirstMainActivities(StickySelector(rng, LEAD))
        mobilityPlan.assignSecondaryMainActivities(StickySelector(rng, MAJOR))
        mobilityPlan.assignMinorActivities(AssignMinorActivityDuration(rng))

    }
}

fun interface MobilityPlanStartTimeAssignment {
    fun assignStartTimes(mobilityPlan: MobilityPlan)
}

class StandardStartTimeAssignment(val rng: RNGHelper) : MobilityPlanStartTimeAssignment {
    override fun assignStartTimes(mobilityPlan: MobilityPlan) {
        val preferredHistogram = mobilityPlan.assignPreferredTourStart(StandardPreferredTourStart(rng))

        val firstStrategy = TourStartWithPreference(
            rng = rng,
            startTimeHistograms = FIRST_TOUR_HISTOGRAM,
            preferredTourStart = preferredHistogram,
            strategy = PreferredStartViaChoiceModel(rng),
        )

        val secondStrategy = TourStartWithPreference(
            rng = rng,
            startTimeHistograms = SECOND_TOUR_HISTOGRAM,
            preferredTourStart = preferredHistogram,
            strategy = UsePreferredTourStart.DISABLED
        )
        mobilityPlan.assignFirstTourStarts(firstStrategy)
        mobilityPlan.assignSecondTourStarts(secondStrategy)
        mobilityPlan.assignRemainingTourStarts(TourStartByHistogramsRelative.standard(rng))
    }

}
