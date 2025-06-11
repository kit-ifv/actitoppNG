package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.TourAmountSet
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultTourAmountParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.TourAmountParameters


val step1KWithParams = ModifiableDiscreteChoiceModel<Int, PersonSituation, TourAmountSet>(AllocatedLogit.create {
    option(1) {
        0.0
    }
    option(2, parameters = {option2}) { standardUtilityFunction(this, it) }
    option(3, parameters = {option3}) { standardUtilityFunction(this, it) }
    option(4, parameters = {option4}) { standardUtilityFunction(this, it) }
}
).initializeWithParameters(DefaultTourAmountParameters)


private val standardUtilityFunction:  TourAmountParameters.(PersonSituation) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFulltime+
                (it.isParttimeEmployee()) * employmentParttime+
                (it.isStudent()) * employmentStudent+
                (it.isAged26To35()) * ageIn26To35+
                (it.isAged36To50()) * ageIn36To50+
                (it.isAged51To60()) * ageIn51To60+
                (it.isAged61To70()) * ageIn61To70+
                (it.areaTypeConurbation()) * areaTypeConurbation+
                (it.areaTypeRural()) * areaTypeRural+
                (it.commuteOver50km()) * commuteOver50km+
                (it.commuteIn0To5km()) * commuteIn0To5Km+
                (it.hasChildrenInHousehold()) * householdHasChildren+
                (it.isMale()) * isMale
}
