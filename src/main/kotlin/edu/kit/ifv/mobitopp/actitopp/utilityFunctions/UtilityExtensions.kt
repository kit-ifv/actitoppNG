package edu.kit.ifv.mobitopp.actitopp.utilityFunctions

import discreteChoice.EnumeratedDiscreteChoiceModel
import discreteChoice.models.ChoiceAlternative
import kotlin.random.Random

fun <R : Any, A : ChoiceAlternative<R>, P> EnumeratedDiscreteChoiceModel<R, A, P>.select(random: Random, converter: (R) -> A): R {
    return select(choices.map(converter).toSet(), random)

}

fun <R : Any, A : ChoiceAlternative<R>, P> EnumeratedDiscreteChoiceModel<R, A, P>.selectInjected(choices: Set<A>, injections: Map<R, (Double) -> Double>, random: Random): R {
    return model.selectInjected(choices, injections, random)

}