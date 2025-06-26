package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultServiceParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ServiceDayParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ServiceDaySet

val step1EWithParams = DiscreteStructure<Int, PersonAlternative, ServiceDaySet> {

    option(1, parameters = { option1 }) { standardUtilityFunction(this, it) }
    option(2, parameters = { option2 }) { standardUtilityFunction(this, it) }
    option(3, parameters = { option3 }) { standardUtilityFunction(this, it) }
    option(4, parameters = { option4 }) { standardUtilityFunction(this, it) }
    option(5, parameters = { option5 }) { standardUtilityFunction(this, it) }
    option(6, parameters = { option6 }) { standardUtilityFunction(this, it) }
    option(7, parameters = { option7 }) { standardUtilityFunction(this, it) }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of service/transport days in week routine").build(DefaultServiceParameters)

private val standardUtilityFunction: ServiceDayParameters.(PersonAlternative) -> Double = {
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
