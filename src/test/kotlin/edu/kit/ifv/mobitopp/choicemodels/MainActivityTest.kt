package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitopp.randomPersonWithRoutine

import kotlinx.serialization.KSerializer
import java.time.DayOfWeek

class MainActivityTest() : ChoiceModelTest<ActivityType, DayAlternative>(mainActivityChoiceModel) {
    override val serializer: KSerializer<ActivityType> = ActivityType.serializer()


    override val name: String = "mainActivityChoiceModel"

    override fun converter(option: ActivityType): DayAlternative {
        return DayAlternative(option, randomPersonWithRoutine(inputRandom), DayOfWeek.entries.random(inputRandom))
    }
}