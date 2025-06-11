package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ServiceDaySet
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultServiceParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ServiceDayParameters

val step1EWithParams = ModifiableDiscreteChoiceModel<Int, PersonSituation, ServiceDaySet>(AllocatedLogit.create {

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
).initializeWithParameters(DefaultServiceParameters)

private val standardUtilityFunction:  ServiceDayParameters.(PersonSituation) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentIsFulltime +
            (it.isParttimeEmployee()) * employmentIsParttime +
            (it.isAged10To17()) * ageIn10to17 +
            (it.isAged26To35()) * ageIn26to35 +
            (it.isAged36To50()) * ageIn36to50 +
            (it.areaTypeConurbation()) * areaTypeIsConurbation +
            (it.hasChildrenInHousehold()) * householdHasChildren +
            (it.amountOfYouthsInHousehold()) * householdAmountYouths +
            (it.isMale()) * isMale

}
