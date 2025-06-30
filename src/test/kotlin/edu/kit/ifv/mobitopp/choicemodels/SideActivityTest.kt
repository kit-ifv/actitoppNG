package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.ActivityAlternative
import edu.kit.ifv.mobitopp.actitopp.randomDayStructure
import edu.kit.ifv.mobitopp.actitopp.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.randomPlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.utils.Position
import kotlinx.serialization.KSerializer

class SideActivityTest : ChoiceModelTest<ActivityType, ActivityAlternative>(sideActivityChoiceModel) {
    override val name: String = "sideActivityChoiceModel"
    override val serializer: KSerializer<ActivityType> = ActivityType.serializer()

    override fun converter(option: ActivityType): ActivityAlternative {
        val randomDayStructure = randomDayStructure(inputRandom)
        return ActivityAlternative(option,
            personWithRoutine = randomPersonWithRoutine(inputRandom),
            dayStructure = randomDayStructure,
            tourStructure = randomDayStructure.indexedElements().random(inputRandom),
            position = Position.entries.random(inputRandom),
            plannedTourAmounts = randomPlannedTourAmounts(inputRandom),
            )
    }
}