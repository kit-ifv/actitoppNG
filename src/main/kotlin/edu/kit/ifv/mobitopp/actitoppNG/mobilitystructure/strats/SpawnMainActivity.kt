package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.Step2Tracking
import kotlin.random.Random

fun interface SpawnMainActivity {
    context(rng: Random, params: PlanGenerationParameters, choiceModels: AllChoiceModels)
    fun generateNewDay(mobilityStructure: MobilityStructure)
}

class SpawnWithRespect() : SpawnMainActivity {
    context(rng: Random, params: PlanGenerationParameters, choiceModels: AllChoiceModels)
    override fun generateNewDay(mobilityStructure: MobilityStructure) {
        val nextDay = mobilityStructure.nextDay()
        val availableOptions = Step2Tracking.determineAvailableOptions(
            mobilityStructure.getTracker(),
            mobilityStructure.weekRoutine,
            choiceModels.mainActivityChoiceModel.choices
        )
        val activityType = context(DayAlternative(mobilityStructure.weekRoutine, nextDay.weekday), rng) {
            choiceModels.mainActivityChoiceModel.select(availableOptions)
        }
        mobilityStructure.add(nextDay, activityType)

    }
}