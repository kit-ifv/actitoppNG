package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.ActivityAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomDayStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.randomPlannedTourAmounts
import edu.kit.ifv.mobitopp.actitoppNG.utils.Position
import kotlinx.serialization.KSerializer

class SideActivityTest : ChoiceModelTest<ActivityType, ActivityAlternative>(sideActivityChoiceModel) {
    override val name: String = "sideActivityChoiceModel"
    override val serializer: KSerializer<ActivityType> = ActivityType.serializer()

    override fun converter(): ActivityAlternative {
        val randomDayStructure = randomDayStructure(inputRandom)
        return ActivityAlternative(
            personWithRoutine = randomPersonWithRoutine(inputRandom),
            dayStructure = randomDayStructure,
            tourStructure = randomDayStructure.indexedElements().random(inputRandom),
            position = Position.entries.random(inputRandom),
            plannedTourAmounts = randomPlannedTourAmounts(inputRandom),
            )
    }
}