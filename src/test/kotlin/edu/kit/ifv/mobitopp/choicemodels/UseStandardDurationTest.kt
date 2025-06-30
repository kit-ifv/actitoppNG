package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitoppNG.randomMobilityPlanInput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

class UseStandardDurationTest: ChoiceModelTest<Boolean, BooleanDecisionAlternative>(firstActivityUsesStandardDuration) {
    override val name: String = "useStandardDuration"

    override fun converter(option: Boolean): BooleanDecisionAlternative {
        return BooleanDecisionAlternative(option, randomMobilityPlanInput(inputRandom))
    }

    override val serializer: KSerializer<Boolean> = Boolean.serializer()
}