package edu.kit.ifv.mobitopp.actitopp.steps.step1

import edu.kit.ifv.mobitopp.actitopp.ActiToppHousehold
import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.ActitoppPersonModifierFields
import edu.kit.ifv.mobitopp.actitopp.WeekRoutine
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.toModifiable
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ParametrizedDiscreteChoiceModel

fun ActitoppPerson.assignWeekRoutine(rng: RNGKeeper): WeekRoutine {
    return toModifiable().run {
        amountOfWorkingDays = step1AWithParams.select(rng.pull("1A"), this)
        amountOfEducationDays = step1BWithParams.select(rng.pull("1B"), this)
        amountOfLeisureDays = step1CWithParams.select(rng.pull("1C"), this)
        amountOfShoppingDays = step1DWithParams.select(rng.pull("1D"), this)
        amountOfServiceDays = step1EWithParams.select(rng.pull("1E"), this)
        amountOfImmobileDays = step1FWithParams.select(rng.pull("1F"), this)
        averageAmountOfTours = step1KWithParams.select(rng.pull("1K"), this)
        averageAmountOfActivities = step1LWithParams.select(rng.pull("1L"), this)
        toWeekRoutine()
    }
}

fun <T> ParametrizedDiscreteChoiceModel<Int, PersonSituation, T>.select(rand: Double, modifierField: ActitoppPersonModifierFields): Int {
    return select(rand) {PersonSituation(it, modifierField)}
}