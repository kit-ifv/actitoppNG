package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure

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