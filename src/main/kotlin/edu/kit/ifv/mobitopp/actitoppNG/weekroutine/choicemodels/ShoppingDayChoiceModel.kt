package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultShoppingParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.ShoppingDaySet

/**
 * THe choice model determining the amount of days with a SHOPPING activity within the week routine. The default option
 * is 0. The other options [1, 7] share a common utility function.
 */
val shoppingDaysChoiceModel = DiscreteStructure<Int, PersonAlternative, ShoppingDaySet> {
    forOptions((1..7)) {
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
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of shopping days in week routine").build(DefaultShoppingParameters)
