package edu.kit.ifv.mobitopp.actitoppNG.modernization


import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.LegacySpawnSideTours
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.SpawnSideTours
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.SpawnWeek
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.StandardImplementation
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitoppNG.modernization.plan.StandardCommuteDurations
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.*
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.FinalizedActivityPattern
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramPerActivity
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.*
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.generateWeekRoutine
import kotlin.random.Random


fun interface MobilityPlanGeneration {
    context(rng: Random, models: AllChoiceModels)
    fun generate(person: Person, amountOfDays: Int): MobilityPlan
    context(rng: Random, models: AllChoiceModels)
    fun generate(person: Person) = generate(person, 7)
    context(models: AllChoiceModels)
    fun generate(person: Person, amountOfDays : Int = 7, randomOffset: Long = 0L) = context(person.spawnRandomGenerator(randomOffset)) {generate(person, amountOfDays)}
}

class DefaultPlanGeneration(choiceModels: AllChoiceModels) : MobilityPlanGeneration {
    val structureGenerator = StandardStructureGeneration(choiceModels)
    val durationGenerator = StandardDurationAssignment(choiceModels)
    val startTimeGenerator = StandardStartTimeAssignment()
    context(rng: Random, models: AllChoiceModels)
    override fun generate(person: Person, amountOfDays: Int): MobilityPlan {
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
    choiceModels: AllChoiceModels,
    private val histograms: HistogramPerActivity = choiceModels.histograms
) : MobilityPlanGeneration {
    private val sideActivityStrategy = StandardImplementation()
    private val spawnMainActivities = SpawnWeek()
    private val spawnSideTours: SpawnSideTours = LegacySpawnSideTours()

    context(rng: Random, models: AllChoiceModels)
    override fun generate(person: Person, amountOfDays: Int): MobilityPlan {

        val weekRoutine = person.generateWeekRoutine()
        val mobilityStructure = MobilityStructure(person, weekRoutine)
        // Generate the main activities of each day
        spawnMainActivities.spawnMainActivities(mobilityStructure)
        // Determine the amount of tours that precede & succeed the main tour.
        spawnSideTours.spawnSideTours(mobilityStructure)
        sideActivityStrategy.spawnSideActivities(mobilityStructure)
        // Determine the amount of precursor and successor tours for each tour of the day

        val budget = histograms.determineTimeBudgets(
            person,
            FinalizedActivityPattern.fromModernPattern(mobilityStructure)
        )
        return mobilityStructure.toPlan(StandardCommuteDurations.STANDARD_ASSIGNMENT, budget)
    }
}

fun interface MobilityPlanDurationAssignment {
    context(rng: Random, models: AllChoiceModels)
    fun assignDurations(mobilityPlan: MobilityPlan)
}

class StandardDurationAssignment(models: AllChoiceModels) : MobilityPlanDurationAssignment {
    private val util = UtilityFunctionAssignment(models)
    private val minorActivityAssignStrategy = AssignMinorActivityDuration(models)
    context(rng: Random, models: AllChoiceModels)
    override fun assignDurations(mobilityPlan: MobilityPlan) {

        mobilityPlan.assignFirstMainActivities(StickySelector( models.leadActivityDurationChoiceModel,util))
        mobilityPlan.assignSecondaryMainActivities(StickySelector( models.majorActivityDurationChoiceModel, util))
        mobilityPlan.assignMinorActivities(minorActivityAssignStrategy)
    }
}

fun interface MobilityPlanStartTimeAssignment {
    context(rng: Random, models: AllChoiceModels)
    fun assignStartTimes(mobilityPlan: MobilityPlan)
}

class StandardStartTimeAssignment() : MobilityPlanStartTimeAssignment {
    private val preferredTourStart = StandardPreferredTourStart()
    private val firstTourStrategy = PreferredStartViaChoiceModel()
    private val secondTourStrategy = UsePreferredTourStart.DISABLED
    context(rng: Random, models: AllChoiceModels)
    override fun assignStartTimes(mobilityPlan: MobilityPlan) {
        val preferredHistogram = mobilityPlan.assignPreferredTourStart(preferredTourStart)

        val firstStrategy = TourStartWithPreference(
            startTimeHistograms = models.firstTourHistogram,
            preferredTourStart = preferredHistogram,
            strategy = firstTourStrategy
        )

        val secondStrategy = TourStartWithPreference(
            startTimeHistograms = models.secondTourHistogram,
            preferredTourStart = preferredHistogram,
            strategy = secondTourStrategy
        )
        mobilityPlan.assignFirstTourStarts(firstStrategy)
        mobilityPlan.assignSecondTourStarts(secondStrategy)
        mobilityPlan.assignRemainingTourStarts(TourStartByHistogramsRelative.standard())
    }

}
