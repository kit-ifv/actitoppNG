package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.DaySituation
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.Step2Tracking
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine
import edu.kit.ifv.mobitopp.main

fun interface SpawnMainActivity {
    fun generateNewDay(mobilityStructure: MobilityStructure)
}

class SpawnWithRespect(val rng: RNGHelper) : SpawnMainActivity {
    override fun generateNewDay(mobilityStructure: MobilityStructure) {
        val nextDay = mobilityStructure.nextDay()
        val availableOptions = Step2Tracking.determineAvailableOptions(
            mobilityStructure.getTracker(),
            mobilityStructure.weekRoutine,
            mainActivityChoiceModel.registeredOptions()
        )
        val activityType =
            mainActivityChoiceModel.select(
                randomNumber = rng.randomValue,
                options = availableOptions
            ) { DaySituation(it, mobilityStructure.weekRoutine, nextDay.weekday) }
        mobilityStructure.add(nextDay, activityType)

    }
}