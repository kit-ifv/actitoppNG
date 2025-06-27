package edu.kit.ifv.mobitopp.choicemodels

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.educationDaysChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.leisureDaysChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.shoppingDaysChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.serviceDaysChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.homeDaysChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.tourAmountChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.activityAmountChoiceModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

abstract class RoutineTest(choiceModel: EnumeratedDiscreteChoiceModel<Int, PersonAlternative, *>):
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

class EducationTest: RoutineTest(educationDaysChoiceModel) {
    override val name: String = "educationTest"
}
class ActivityAmountTest: RoutineTest(activityAmountChoiceModel) {
    override val name: String = "activityAmountTest"
}
class ImmobileTest: RoutineTest(homeDaysChoiceModel) {
    override val name: String = "immobileTest"
}

class LeisureTest: RoutineTest(leisureDaysChoiceModel) {
    override val name: String = "leisureTest"
}
class ServiceTest: RoutineTest(serviceDaysChoiceModel) {
    override val name: String = "serviceTest"
}
class ShoppingTest: RoutineTest(shoppingDaysChoiceModel) {
    override val name: String = "shoppingTest"
}

class TourAmountTest: RoutineTest(tourAmountChoiceModel) {
    override val name: String = "tourAmountTest"
}