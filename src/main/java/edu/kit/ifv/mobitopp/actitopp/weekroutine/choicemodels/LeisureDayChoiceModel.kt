package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.LeisureDaySet
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultLeisureParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.LeisureDayParameters

val step1CWithParams = ModifiableDiscreteChoiceModel<Int, PersonSituation, LeisureDaySet>(AllocatedLogit.create {

    option(1, parameters = {option1}) { standardUtilityFunction(this, it) }
    option(2, parameters = {option2}) { standardUtilityFunction(this, it) }
    option(3, parameters = {option3}) { standardUtilityFunction(this, it) }
    option(4, parameters = {option4}) { standardUtilityFunction(this, it) }
    option(5, parameters = {option5}) { standardUtilityFunction(this, it) }
    option(6, parameters = {option6}) { standardUtilityFunction(this, it) }
    option(7, parameters = {option7}) { standardUtilityFunction(this, it) }
    option(0) {
        0.0
    }
}
).initializeWithParameters(DefaultLeisureParameters)
private val standardUtilityFunction:  LeisureDayParameters.(PersonSituation) -> Double = {
    base +
            (it.isEarningMoney()) * employmentIsEarning +
            (it.isStudent()) * emplomentStudent +
            (it.areaTypeRural()) * areaTypeIsRural +
            (it.hasChildrenInHousehold()) * householdHasChildenBelowAge10 +
            (it.isAged61To70()) * ageIn61To70 +
            it.amountOfWorkingDays() * amountOfWorkingDays
}
