package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultEducationParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.EducationDaySet

/**
 * The choice model of selecting the amount of days with an education activity in the week routine. The default option
 * is 0 days. The other options [1, 7] share a common utility function.
 */
val educationDaysChoiceModel = DiscreteStructure<Int, PersonAlternative, EducationDaySet> {
    forOptions(1..7) {
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
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of education days in week routine")
    .build(DefaultEducationParameters)
