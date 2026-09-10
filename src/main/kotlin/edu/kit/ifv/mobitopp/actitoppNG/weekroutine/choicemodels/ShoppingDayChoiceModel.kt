package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isEarning
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.ShoppingDaySet
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

/**
 * THe choice model determining the amount of days with a SHOPPING activity within the week routine. The default option
 * is 0. The other options [1, 7] share a common utility function.
 */
context(params: PlanGenerationParameters)
val shoppingDaysChoiceModel get() = DiscreteStructure<Int, PersonAlternative, ShoppingDaySet> {
    loadOptionsMap((1..7)) {_, it ->
        base +
                when(it.employment) {
                    Employment.VOCATIONAL -> employmentVocational
                    in isEarning -> employmentIsEarning
                    in isStudent -> emplomentStudent
                    else -> 0.0
                } +
                when(it.areaType) {
                    AreaType.RURAL ->  areaTypeIsRural
                    AreaType.CONURBATION ->  areaTypeIsConurbation
                    else -> 0.0
                } +
                when (it.ageCode) {
                    AgeEncoder.isAged10To17 -> ageIn10To17
                    else -> 0.0
                } +
                it.amountOfWorkingDays() * amountOfWorkingDays +
                it.amountOfEducationDays() * amountOfEducationDays +
                it.amountOfLeisureDays() * amountOfLeisureDays
    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of shopping days in week routine")
    .build(params.shoppingDayParams)
