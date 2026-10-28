package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
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
                when (it.ageCode) {
                    AgeEncoder.isAged10To17 -> ageIn10to17
                    AgeEncoder.isAged18To25-> ageIn18To25
                    AgeEncoder.isAged26To35 -> ageIn26To35
                    AgeEncoder.isAged36To50 -> ageIn36To50
                    AgeEncoder.isAged51To60 -> ageIn51to60
                    AgeEncoder.isAged61To70 -> ageIn61to70
                    else -> 0.0
                } +
                when(it.areaType) {
                    AreaType.CONURBATION -> areaTypeConurburation
                    AreaType.RURAL -> areaTypeRural
                    else -> 0.0
                } +
                (it.hasChildrenInHousehold) * householdHasChildenBelowAge10 +
                (it.isMale()) * genderIsMale
    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of workdays in week routine")
    .build(params.workDayParams)