package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.weekroutine.WeekRoutine

fun interface SpawnMainActivities {
    fun spawnMainActivities(input: MobilityStructure)
}

class SpawnWeek(
    val rng: RNGHelper,
    val person: IPerson,
    val routine: WeekRoutine,
    val dayStrategy: SpawnMainActivity = SpawnWithRespect(rng, person, routine)
): SpawnMainActivities {
    override fun spawnMainActivities(input: MobilityStructure) {
        repeat(7) {
            dayStrategy.generateNewDay(input)
        }
    }
}