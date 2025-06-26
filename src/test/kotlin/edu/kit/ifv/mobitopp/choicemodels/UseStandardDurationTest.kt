package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitopp.randomMobilityPlanInput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class UseStandardDurationTest: ChoiceModelTest<Boolean, BooleanDecisionAlternative>(firstActivityUsesStandardDuration) {
    override val name: String = "useStandardDuration"

    override fun converter(option: Boolean): BooleanDecisionAlternative {
        return BooleanDecisionAlternative(option, randomMobilityPlanInput(inputRandom))
    }

    override val serializer: KSerializer<Boolean> = Boolean.serializer()
}