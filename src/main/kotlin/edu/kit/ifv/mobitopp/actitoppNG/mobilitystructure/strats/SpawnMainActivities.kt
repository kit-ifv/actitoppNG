package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.modernization.MobilityStructure

fun interface SpawnMainActivities {
    fun spawnMainActivities(input: MobilityStructure)
}

class SpawnWeek(
    val rng: RNGHelper,
    private val dayStrategy: SpawnMainActivity = SpawnWithRespect(rng),
) : SpawnMainActivities {
    override fun spawnMainActivities(input: MobilityStructure) {
        repeat(7) {
            dayStrategy.generateNewDay(input)
        }
    }
}