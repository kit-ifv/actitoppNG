package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.SideTourMainActivityGenerator
import edu.kit.ifv.mobitopp.actitoppNG.modernization.calculateTourAmounts
import kotlin.random.Random

fun interface SpawnSideTours {
    context(rng: Random, params: PlanGenerationParameters, models: AllChoiceModels)
    fun spawnSideTours(input: MobilityStructure)
}

class LegacySpawnSideTours() : SpawnSideTours {
    context(rng: Random, params: PlanGenerationParameters, models: AllChoiceModels)
    override fun spawnSideTours(input: MobilityStructure) {

        val plannedTourAmounts = input.calculateTourAmounts()
        val tourSpawner = SideTourMainActivityGenerator(input)
        tourSpawner.loadSideTours(plannedTourAmounts)
    }
}