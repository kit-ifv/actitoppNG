package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultShoppingParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ShoppingDayParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ShoppingDaySet

val step1DWithParams = DiscreteStructure<Int, PersonAlternative, ShoppingDaySet> {

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
}.multinomialLogit("Amount of shopping days in week routine").build(DefaultShoppingParameters)
private val standardUtilityFunction: ShoppingDayParameters.(PersonAlternative) -> Double = {
    base +
            (it.isEarningMoney()) * employmentIsEarning +
            (it.isStudent()) * emplomentStudent +
            (it.isVocational()) * employmentVocational +
            (it.areaTypeConurbation()) * areaTypeIsConurbation +
            (it.areaTypeRural()) * areaTypeIsRural +
            (it.isAged10To17()) * ageIn10To17 +
            it.amountOfWorkingDays() * amountOfWorkingDays +
            it.amountOfEducationDays() * amountOfEducationDays +
            it.amountOfLeisureDays() * amountOfLeisureDays
}
