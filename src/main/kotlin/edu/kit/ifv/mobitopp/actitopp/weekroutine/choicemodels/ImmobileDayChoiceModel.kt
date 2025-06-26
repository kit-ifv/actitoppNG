package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParameterCollectionStep1F
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParameterSet1F
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParametersStep1F


val step1FWithParams =
    DiscreteStructure<Int, PersonAlternative, ParameterCollectionStep1F> {

        option(1, parameters = { option1 }) { standardUtilityFunction(this, it) }
        option(2, parameters = { option2 }) { standardUtilityFunction(this, it) }
        option(3, parameters = { option3 }) { standardUtilityFunction(this, it) }
        option(4, parameters = { option4 }) { standardUtilityFunction(this, it) }
        option(5, parameters = { option5 }) { standardUtilityFunction(this, it) }
        option(6, parameters = { option6 }) { standardUtilityFunction(this, it) }
        option(7, parameters = { option7 }) {
            standardUtilityFunction(this, it)
        }
        option(0) {
            0.0
        }
    }.multinomialLogit("Amount of immobile days (Home days) in week routine").build(ParameterSet1F)


private val standardUtilityFunction: ParametersStep1F.(PersonAlternative) -> Double = {
    base +
            (it.isNotEarningMoney()) * employmentNotEarning +
            (it.isRetired()) * employmentRetired +
            (it.isAged18To25()) * ageIn18To25 +
            it.amountOfYouthsInHousehold() * amountOfYouths +
            (it.isMale()) * isMale +
            it.amountOfWorkingDays() * amountOfWorkingDays +
            it.amountOfEducationDays() * amountOfEducationDays +
            it.amountOfLeisureDays() * amountOfLeisureDays +
            it.amountOfShoppingDays() * amountOfShoppingDays
}
