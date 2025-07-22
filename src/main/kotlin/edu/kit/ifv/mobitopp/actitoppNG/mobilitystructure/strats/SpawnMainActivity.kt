package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.Step2Tracking

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
        val activityType = context(DayAlternative(mobilityStructure.weekRoutine, nextDay.weekday), rng) {
            mainActivityChoiceModel.select(availableOptions)
        }
        mobilityStructure.add(nextDay, activityType)

    }
}