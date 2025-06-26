package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultEducationParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.EducationDayParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.EducationDaySet


val step1BWithParams = DiscreteStructure<Int, PersonAlternative, EducationDaySet> {

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
}.multinomialLogit("Amount of education days in week routine")
.build(DefaultEducationParameters)


private val standardUtilityFunction: EducationDayParameters.(PersonAlternative) -> Double = {
    base +
            (it.isEarningMoney()) * employmentIsEarning +
            (it.isStudent()) * emplomentStudent +
            (it.isVocational()) * employmentVocational +
            (it.isAged10To17()) * ageIn10to17 +
            (it.isAged18To25()) * ageIn18To25 +
            (it.isAged26To35()) * ageIn26To35 +
            (it.isAged36To50()) * ageIn36To50 +
            it.amountOfWorkingDays() * amountOfWorkingDays
}
