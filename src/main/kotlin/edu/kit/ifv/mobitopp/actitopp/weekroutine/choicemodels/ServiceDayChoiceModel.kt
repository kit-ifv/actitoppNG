package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultServiceParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ServiceDaySet

/**
 * The choice model determining the amount of days with a SHOPPING activity within the week routine. The default option
 * is 0. The other options [1,7] share a common utility function.
 */
val serviceDaysChoiceModel = DiscreteStructure<Int, PersonAlternative, ServiceDaySet> {
    forOptions(1..7) {
        base +
                (it.isFulltimeEmployee()) * employmentIsFulltime +
                (it.isParttimeEmployee()) * employmentIsParttime +
                (it.isAged10To17()) * ageIn10to17 +
                (it.isAged26To35()) * ageIn26to35 +
                (it.isAged36To50()) * ageIn36to50 +
                (it.areaTypeConurbation()) * areaTypeIsConurbation +
                (it.hasChildrenInHousehold()) * householdHasChildren +
                (it.amountOfYouthsInHousehold()) * householdAmountYouths +
                (it.isMale()) * isMale

    }
    option(0) {
        0.0
    }
}.multinomialLogit("Amount of service/transport days in week routine").build(DefaultServiceParameters)
