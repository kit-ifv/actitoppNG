package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.STATIC_HISTOGRAMS
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.SpawnWithRespect
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.StandardCommuteDurations
import edu.kit.ifv.mobitopp.actitopp.spawnRandomGenerator

import edu.kit.ifv.mobitopp.actitopp.weekroutine.generateWeekRoutine
import edu.kit.ifv.mobitopp.actitopp.steps.step10.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.steps.step10.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.steps.step10.TourStartByHistogramsRelative
import edu.kit.ifv.mobitopp.actitopp.steps.step10.TourStartWithPreference
import edu.kit.ifv.mobitopp.actitopp.steps.step10.assignFirstTourStarts
import edu.kit.ifv.mobitopp.actitopp.steps.step10.assignRemainingTourStarts
import edu.kit.ifv.mobitopp.actitopp.steps.step10.assignSecondTourStarts
import edu.kit.ifv.mobitopp.actitopp.steps.step7.FinalizedActivityPattern
import edu.kit.ifv.mobitopp.actitopp.steps.step8.AssignMinorActivityDuration
import edu.kit.ifv.mobitopp.actitopp.steps.step8.LEAD
import edu.kit.ifv.mobitopp.actitopp.steps.step8.MAJOR
import edu.kit.ifv.mobitopp.actitopp.steps.step8.StandardStep8B
import edu.kit.ifv.mobitopp.actitopp.steps.step8.assignFirstMainActivities
import edu.kit.ifv.mobitopp.actitopp.steps.step8.assignMinorActivities
import edu.kit.ifv.mobitopp.actitopp.steps.step8.assignSecondaryMainActivities
import edu.kit.ifv.mobitopp.actitopp.steps.step9.StandardPreferredTourStart
import edu.kit.ifv.mobitopp.actitopp.steps.step9.assignPreferredTourStart


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
    override fun generate(person: IPerson, amountOfDays: Int): MobilityPlan {

        val weekRoutine = person.generateWeekRoutine(rng)
        val mobilityStructure = MobilityStructure(person, weekRoutine)
        // Generate the main activities of each day
        val mainGenerator = SpawnWithRespect(rng, person, weekRoutine)
        repeat(amountOfDays) {
            mainGenerator.generateNewDay(mobilityStructure)
//            mobilityStructure.determineNextMainActivity(rngKeeper = rng)
        }
        // Determine the amount of tours that precede & succeed the main tour.
        val tourOutput = mobilityStructure.calculateTourAmounts(rngHelper = rng)

        // Determine the main type of the subtours and load it into the pattern.
        val generator = SideTourMainActivityGenerator(mobilityStructure, rng)
        generator.loadSideTours(tourOutput)

        // Determine the amount of precursor and successor tours for each tour of the day
        val step5Gen = Step5Generator(mobilityStructure, rng)
        step5Gen.calculate()
        val step5output = step5Gen.output()
        val nextStep = ExampleAssign(mobilityStructure, rng)
        mobilityStructure.elements().forEach { day ->
            day.trackedElements().forEach { element ->
                val plan = step5output[day]?.get(element)?: PlannedTourAmounts.NONE
                val (precursors, successors) = nextStep.generateSecondaryActivityTypes(SecondaryActInput(day, element, plan))
                element.element.loadPrecursors(precursors)
                element.element.loadSuccessors(successors)
            }
        }


//        step5output.assignDirectly(nextStep)


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
