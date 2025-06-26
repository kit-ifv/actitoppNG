package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultTourAmountParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.TourAmountParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.TourAmountSet


val step1KWithParams = DiscreteStructure<Int, PersonAlternative, TourAmountSet> {
    option(1) {
        0.0
    }
    option(2, parameters = { option2 }) { standardUtilityFunction(this, it) }
    option(3, parameters = { option3 }) { standardUtilityFunction(this, it) }
    option(4, parameters = { option4 }) { standardUtilityFunction(this, it) }
}.multinomialLogit("Average amount of tours per day in week routine ").build(DefaultTourAmountParameters)


private val standardUtilityFunction: TourAmountParameters.(PersonAlternative) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFulltime +
            (it.isParttimeEmployee()) * employmentParttime +
            (it.isStudent()) * employmentStudent +
            (it.isAged26To35()) * ageIn26To35 +
            (it.isAged36To50()) * ageIn36To50 +
            (it.isAged51To60()) * ageIn51To60 +
            (it.isAged61To70()) * ageIn61To70 +
            (it.areaTypeConurbation()) * areaTypeConurbation +
            (it.areaTypeRural()) * areaTypeRural +
            (it.commuteOver50km()) * commuteOver50km +
            (it.commuteIn0To5km()) * commuteIn0To5Km +
            (it.hasChildrenInHousehold()) * householdHasChildren +
            (it.isMale()) * isMale
}
