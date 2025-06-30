package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultWorkParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.WorkDaySet

/**
 * The choice model determining the amount of work days with a WORK activity within the week routine. The default option
 * is 0. The other options [1, 7] share a common utility function.
 */
val defaultWorkDayChoiceModel = DiscreteStructure<Int, PersonAlternative, WorkDaySet> {
    forOptions(1..7) {
        base +
                (it.isFulltimeEmployee()) * employmentFullTime +
                (it.isParttimeEmployee()) * employmentPartTime +
                (it.isStudent()) * employmentStudent +
                (it.isVocational()) * employmentVocational +
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
    option(0) {
        0.0
    }

}.multinomialLogit("Amount of workdays in week routine").build(DefaultWorkParameters)