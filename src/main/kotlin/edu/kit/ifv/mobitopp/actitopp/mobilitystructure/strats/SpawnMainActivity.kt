package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.Step2Tracking

fun interface SpawnMainActivity {
    fun generateNewDay(mobilityStructure: MobilityStructure)
}

class SpawnWithRespect(val rng: RNGHelper) : SpawnMainActivity {
    override fun generateNewDay(mobilityStructure: MobilityStructure) {
        val nextDay = mobilityStructure.nextDay()
        val availableOptions = Step2Tracking.determineAvailableOptions(
            mobilityStructure.getTracker(),
            mobilityStructure.weekRoutine,
            mainActivityChoiceModel.choices
        )
        val activityType =
            mainActivityChoiceModel.select(
                choices = availableOptions,
                random = rng,

            ) { DayAlternative(it, mobilityStructure.weekRoutine, nextDay.weekday) }
        mobilityStructure.add(nextDay, activityType)

    }
}