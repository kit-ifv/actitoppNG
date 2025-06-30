package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.SideTourMainActivityGenerator
import edu.kit.ifv.mobitopp.actitoppNG.modernization.calculateTourAmounts

fun interface SpawnSideTours {
    fun spawnSideTours(input: MobilityStructure)
}

class LegacySpawnSideTours(val rng: RNGHelper) : SpawnSideTours {

    override fun spawnSideTours(input: MobilityStructure) {

        val plannedTourAmounts = input.calculateTourAmounts(rngHelper = rng)
        val tourSpawner = SideTourMainActivityGenerator(input, rng)
        tourSpawner.loadSideTours(plannedTourAmounts)
    }
}