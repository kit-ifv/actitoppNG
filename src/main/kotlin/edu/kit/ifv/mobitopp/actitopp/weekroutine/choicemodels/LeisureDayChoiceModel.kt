package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultLeisureParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.LeisureDaySet

/**
 * The choice model determining the amount of days with a LEISURE activity within the week routine. The default option is
 * 0. The other options [1, 7] share a common utility function.
 */
val leisureDaysChoiceModel = DiscreteStructure<Int, PersonAlternative, LeisureDaySet> {
    forOptions(1..7) {
        base +
                (it.isEarningMoney()) * employmentIsEarning +
                (it.isStudent()) * emplomentStudent +
                (it.areaTypeRural()) * areaTypeIsRural +
                (it.hasChildrenInHousehold()) * householdHasChildenBelowAge10 +
                (it.isAged61To70()) * ageIn61To70 +
                it.amountOfWorkingDays() * amountOfWorkingDays
    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of leisure days in week routine").build(DefaultLeisureParameters)
