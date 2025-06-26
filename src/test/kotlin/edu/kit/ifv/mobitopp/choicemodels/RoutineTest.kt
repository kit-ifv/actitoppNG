package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitopp.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1BWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1CWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1DWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1EWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1FWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1KWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1LWithParams
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

abstract class RoutineTest(choiceModel: ParametrizedDiscreteChoiceModel<Int, PersonAlternative, *>):
    ChoiceModelTest<Int, PersonAlternative>(choiceModel) {
    override val serializer: KSerializer<Int> = Int.serializer()
        override fun converter(option: Int): PersonAlternative {
            val (person, routine)  = randomPersonWithRoutine(inputRandom)
            return PersonAlternative(option, routine,  person)
    }
}

class WorkTest: RoutineTest(defaultWorkDayChoiceModel) {
    override val name: String = "workTest"
}

class EducationTest: RoutineTest(step1BWithParams) {
    override val name: String = "educationTest"
}
class ActivityAmountTest: RoutineTest(step1LWithParams) {
    override val name: String = "activityAmountTest"
}
class ImmobileTest: RoutineTest(step1FWithParams) {
    override val name: String = "immobileTest"
}

class LeisureTest: RoutineTest(step1CWithParams) {
    override val name: String = "leisureTest"
}
class ServiceTest: RoutineTest(step1EWithParams) {
    override val name: String = "serviceTest"
}
class ShoppingTest: RoutineTest(step1DWithParams) {
    override val name: String = "shoppingTest"
}

class TourAmountTest: RoutineTest(step1KWithParams) {
    override val name: String = "tourAmountTest"
}