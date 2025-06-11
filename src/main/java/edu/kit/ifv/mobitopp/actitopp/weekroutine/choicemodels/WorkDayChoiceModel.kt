package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultWorkParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.WorkDayParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.WorkDaySet


val defaultWorkDayChoiceModel = ModifiableDiscreteChoiceModel<Int, PersonSituation, WorkDaySet>(AllocatedLogit.create {

    option(1, parameters = {option1}, { standardUtilityFunction(this, it) })
    option(2, parameters = {option2}, { standardUtilityFunction(this, it) })
    option(3, parameters = {option3}, { standardUtilityFunction(this, it) })
    option(4, parameters = {option4}, { standardUtilityFunction(this, it) })
    option(5, parameters = {option5}, { standardUtilityFunction(this, it) })
    option(6, parameters = {option6}, { standardUtilityFunction(this, it) })
    option(7, parameters = {option7}, { standardUtilityFunction(this, it) })
    option(0) {
        0.0
    }
    
}).initializeWithParameters(DefaultWorkParameters)

private val standardUtilityFunction:  WorkDayParameters.(PersonSituation) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFullTime +
            (it.isParttimeEmployee()) * employmentPartTime +
            (it.isStudent()) * employmentStudent +
            (it.isVocational()) * employmentVocational+

            (it.isAged10To17()) * ageIn10to17 +
            (it.isAged18To25()) * ageIn18To25 +
            (it.isAged26To35()) * ageIn26To35 +
            (it.isAged36To50()) * ageIn36To50 +
            (it.isAged51To60()) * ageIn51to60 +
            (it.isAged61To70()) * ageIn61to70 +

            (it.areaTypeConurbation()) * areaTypeConurburation +
            (it.areaTypeRural()) * areaTypeRural +
            (it.hasChildrenInHousehold()) * householdHasChildenBelowAge10 +
            (it.isMale()) * genderIsMale
}
