package edu.kit.ifv.mobitopp.actitopp.steps.step10

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.steps.step7.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.steps.step8.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.steps.step8.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.utils.takeUntil
import kotlin.time.Duration


fun interface SelectTourStart {
    fun selectStartTime(input: MobilityPlanInputs): Duration
}


interface SelectTourStartWithPreference : SelectTourStart {
    val preferredTourStart: ArrayHistogram?
    fun selectStartTime(input: MobilityPlanInputs, preferredTourStart: ArrayHistogram?): Duration

    override fun selectStartTime(input: MobilityPlanInputs): Duration {
        return selectStartTime(input, preferredTourStart)
    }
}

fun MobilityPlan.assignFirstTourStarts(strategy: SelectTourStart) {
    dayPlans.forEach { dayPlan ->
        val tourPlan = dayPlan.tourPlans.first()
        val activity = tourPlan.last() // TODO, make it so that input without activities does not need them.
        val tourStartTime = strategy.selectStartTime(MobilityPlanInputs(this, person, dayPlan, tourPlan, activity))


        tourPlan.first().startTime = tourStartTime + dayPlan.durationDay.startOfDay
        val takeUntil = tourPlan.first().mutableIterator().drop(1).takeUntil { it == tourPlan.nextHomeActivity }
        takeUntil.forEach {
            if(it.startTime == null) {
                it.startTime = it.previous?.endTime?: throw NoSuchElementException("Should not be null")
            }
            require(this.isConsistent()) {
                "Mobility plan became inconsistent"
            }
        }

    }
}

fun MobilityPlan.assignSecondTourStarts(strategy: SelectTourStart) {
    dayPlans.forEach { dayPlan ->
        val tourPlan = dayPlan.tourPlans.getOrNull(1)

        tourPlan?.let {

            val activity = it.last() // TODO, make it so that input without activities does not need them.
            val tourStartTime = strategy.selectStartTime(MobilityPlanInputs(this, person, dayPlan, it, activity))
            it.first().startTime = tourStartTime + dayPlan.durationDay.startOfDay
            val takeUntil = tourPlan.first().mutableIterator().drop(1).takeUntil { it == tourPlan.nextHomeActivity }
            takeUntil.forEach {
                if(it.startTime == null) {
                    it.startTime = it.previous?.endTime?: throw NoSuchElementException("Should not be null")
                }
                require(this.isConsistent()) {
                    "Mobility plan became inconsistent"
                }

            }
        }
    }
}

fun MobilityPlan.assignRemainingTourStarts(strategy: SelectTourStart) {
    dayPlans.forEach { dayPlan ->
        dayPlan.tourPlans.drop(2).forEach { tourPlan ->
            val activity = tourPlan.last() // TODO, make it so that input without activities does not need them.
            val tourStartTime = strategy.selectStartTime(MobilityPlanInputs(this, person, dayPlan, tourPlan, activity))


            tourPlan.first().startTime = tourStartTime + dayPlan.durationDay.startOfDay
            tourPlan.first().mutableIterator().drop(1).takeUntil { it == tourPlan.nextHomeActivity }.forEach {
                if(it.startTime == null) {
                    it.startTime = it.previous?.endTime?: throw NoSuchElementException("Should not be null")
                }
                require(this.isConsistent()) {
                    "Mobility plan became inconsistent ${this.fullPrint()}"
                }
            }
        }
    }
}