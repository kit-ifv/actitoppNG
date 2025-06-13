package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParameterCollectionStep1F
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParameterSet1F
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ParametersStep1F


val step1FWithParams =
    ModifiableDiscreteChoiceModel<Int, PersonSituation, ParameterCollectionStep1F>(AllocatedLogit.create {

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
    }
    ).initializeWithParameters(ParameterSet1F)


private val standardUtilityFunction: ParametersStep1F.(PersonSituation) -> Double = {
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
