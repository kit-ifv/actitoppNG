package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.SideTourMainActivityGenerator
import edu.kit.ifv.mobitopp.actitopp.modernization.calculateTourAmounts

fun interface SpawnSideTours {
    fun spawnSideTours(input: MobilityStructure)
}

class LegacySpawnSideTours(val rng: RNGHelper): SpawnSideTours {

    override fun spawnSideTours(input: MobilityStructure) {

        val plannedTourAmounts = input.calculateTourAmounts(rngHelper = rng)
        val tourSpawner = SideTourMainActivityGenerator(input, rng)
        tourSpawner.loadSideTours(plannedTourAmounts)
    }
}