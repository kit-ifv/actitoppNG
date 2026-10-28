package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels


import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isEarning
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.EducationDaySet
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

/**
 * The choice model of selecting the amount of days with an education activity in the week routine. The default option
 * is 0 days. The other options [1, 7] share a common utility function.
 */
context(params: PlanGenerationParameters)
val educationDaysChoiceModel get() = DiscreteStructure<Int, PersonAlternative, EducationDaySet> {
    loadOptionsMap(1..7) {_, it ->
        base +
                when(it.employment) {
                    Employment.VOCATIONAL -> employmentVocational
                    in isStudent -> emplomentStudent
                    in isEarning -> employmentIsEarning
                    else -> 0.0
                } +
                when(it.ageCode) {
                    AgeEncoder.isAged10To17 -> ageIn10to17
                    AgeEncoder.isAged18To25 -> ageIn18To25
                    AgeEncoder.isAged26To35-> ageIn26To35
                    AgeEncoder.isAged36To50 -> ageIn36To50
                    else -> 0.0
                } +
                it.amountOfWorkingDays() * amountOfWorkingDays
    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of education days in week routine")
    .build(params.educationDayParams)
