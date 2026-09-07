package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.WorkDaySet
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

/**
 * The choice model determining the amount of work days with a WORK activity within the week routine. The default option
 * is 0. The other options [1, 7] share a common utility function.
 */
context(params: PlanGenerationParameters)
val defaultWorkDayChoiceModel get() = DiscreteStructure<Int, PersonAlternative, WorkDaySet> {
    loadOptionsMap(1..7) {_, it ->
        base +
                when(it.employment) {
                    Employment.FULLTIME -> employmentFullTime
                    in isParttime -> employmentPartTime
                    in isStudent -> employmentStudent
                    Employment.VOCATIONAL -> employmentVocational
                    else -> 0.0
                } +
                when (it.age) {
                    in 10..17 -> ageIn10to17
                    in 18..25 -> ageIn18To25
                    in 26..35 -> ageIn26To35
                    in 36..50 -> ageIn36To50
                    in 51..60 -> ageIn51to60
                    in 61..70 -> ageIn61to70
                    else -> 0.0
                } +
                (it.areaTypeConurbation()) * areaTypeConurburation +
                (it.areaTypeRural()) * areaTypeRural +
                (it.hasChildrenInHousehold()) * householdHasChildenBelowAge10 +
                (it.isMale()) * genderIsMale
    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of workdays in week routine")
    .build(params.workDayParams)