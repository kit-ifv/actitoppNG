package edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultHomeParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.HomeDaySet

/**
 * The choice model for selecting the amount of immobile days in the week routine. An immobile day is a day consisting
 * only of a HOME activity. The default option is 0. The remaining options [1, 7] share a common utility function.
 */
val homeDaysChoiceModel =
    DiscreteStructure<Int, PersonAlternative, HomeDaySet> {
        forOptions(1..7) {
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
        option(0) {
            0.0
        }
    }.multinomialLogit("Amount of immobile days (Home days) in week routine").build(DefaultHomeParameters)



