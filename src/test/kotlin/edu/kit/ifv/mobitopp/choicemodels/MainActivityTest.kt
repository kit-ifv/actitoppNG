package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine

import kotlinx.serialization.KSerializer
import java.time.DayOfWeek

class MainActivityTest() : ChoiceModelTest<ActivityType, DayAlternative>(mainActivityChoiceModel) {
    override val serializer: KSerializer<ActivityType> = ActivityType.serializer()


    override val name: String = "mainActivityChoiceModel"

    override fun converter(): DayAlternative {
        return DayAlternative( randomPersonWithRoutine(inputRandom), DayOfWeek.entries.random(inputRandom))
    }
}