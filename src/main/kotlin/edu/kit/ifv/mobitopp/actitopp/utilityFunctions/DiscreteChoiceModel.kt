package edu.kit.ifv.mobitopp.actitopp.utilityFunctions

import kotlin.random.Random

fun interface SelectionFunction<X> {
    fun calculateSelection(options: Map<X, Double>): X
}

open class DiscreteChoiceModel<X : Any, SIT : ChoiceAlternative<X>, P>(
    open val distributionFunction: ExtractableDistributionFunction<X, SIT, P>,
    open val selectionFunction: SelectionFunction<SIT> = SelectionFunction {
        it.select(
            GlobalRandomizer.nextDouble()
        )
    },
) {

    var callback: (Map<SIT, Double>) -> Unit = {
//        it.keys.forEach { k -> println(k) }
//        println(it.entries.map { it.key.choice to it.value })
    }

    fun select(alternatives: Set<SIT>, parameters: P): X {
        return selectionFunction.calculateSelection(
            distributionFunction.calculateDebug(
                alternatives,
                parameters
            ).also { callback(it) }
        ).choice
    }

    fun select(alternatives: Set<SIT>, parameters: P, randomNumber: Double): X {
        return SelectionFunction<SIT> { it.select(randomNumber) }.calculateSelection(
            distributionFunction.calculateProbabilities(
                alternatives,
                parameters
            )
        ).choice
    }


    fun select(singularOption: SIT, parameters: P): X = select(setOf(singularOption), parameters)

    fun probabilities(alternatives: Set<SIT>, parameters: P) = distributionFunction.calculateProbabilities(
        alternatives,
        parameters
    )
}

class ModifiableDiscreteChoiceModel<X : Any, SIT : ChoiceAlternative<X>, P>(
    override val distributionFunction: ModifiableDistributionFunction<X, SIT, P>,
    override var selectionFunction: SelectionFunction<SIT> = SelectionFunction {
        it.select(
            GlobalRandomizer.nextDouble()
        )
    },
) : DiscreteChoiceModel<X, SIT, P>(
    distributionFunction,
    selectionFunction = selectionFunction
) {


    fun utilities(parameters: P, converter: (X) -> SIT): Map<X, Double> {
        return utilities(distributionFunction.options, parameters, converter)
    }

    fun utilities(options: Set<X>, parameters: P, converter: (X) -> SIT): Map<X, Double> {
        return options.associateWith {
            val alternative = converter(it)
            distributionFunction.translation(alternative).calculateUtility(alternative, parameters)
        }
    }


    fun select(parameters: P, randomNumber: Double, situation: (X) -> SIT): X {
        return select(distributionFunction.options, parameters, randomNumber, situation)
    }

    fun select(options: Set<X>, parameters: P, randomNumber: Double, situation: (X) -> SIT): X {
        return select(options.map { situation(it) }.toSet(), parameters, randomNumber)
    }


    fun select(parameters: P, situation: (X) -> SIT): X {
        return select(distributionFunction.options, parameters, situation)
    }

    fun select(options: Set<X>, parameters: P, situation: (X) -> SIT): X {
        return selectionFunction.calculateSelection(
            distributionFunction.calculateProbabilities(
                options.map(
                    situation
                ).toSet(), parameters
            )
        ).choice
    }

    fun probabilities(parameters: P, converter: (X) -> SIT): Map<X, Double> {
        return probabilities(distributionFunction.options, parameters, converter)
    }

    fun probabilities(options: Set<X>, parameters: P, converter: (X) -> SIT): Map<X, Double> {
        return probabilities(options.map(converter).toSet(), parameters).mapKeys { it.key.choice }
    }

    fun registeredOptions() = distributionFunction.options

    fun utilityFunction(option: X): UtilityFunction<SIT, P> {
        return distributionFunction.translation.getValue(option)
    }
}


interface SealedDiscreteChoiceModel<X : Any, SIT : ChoiceAlternative<X>> {
    fun select(converter: (X) -> SIT): X
    fun select(options: Set<X>, converter: (X) -> SIT): X
    fun select(randomNumber: Double, converter: (X) -> SIT): X
    fun select(options: Set<X>, randomNumber: Double, converter: (X) -> SIT): X
}

class ParametrizedDiscreteChoiceModel<X : Any, SIT : ChoiceAlternative<X>, P>(
    val original: ModifiableDiscreteChoiceModel<X, SIT, P>,
    var parameters: P,
) : SealedDiscreteChoiceModel<X, SIT> {

    override fun select(converter: (X) -> SIT) = original.select(parameters, converter)
    override fun select(options: Set<X>, converter: (X) -> SIT) = original.select(options, parameters, converter)
    override fun select(randomNumber: Double, converter: (X) -> SIT) =
        original.select(parameters, randomNumber, converter)

    override fun select(options: Set<X>, randomNumber: Double, converter: (X) -> SIT) =
        original.select(options, parameters, randomNumber, converter)



    fun registeredOptions() = original.registeredOptions()

}

fun <X : Any, SIT : ChoiceAlternative<X>, PARAMS> ModifiableDiscreteChoiceModel<X, SIT, PARAMS>.initializeWithParameters(
    parameter: PARAMS,
): ParametrizedDiscreteChoiceModel<X, SIT, PARAMS> {
    return ParametrizedDiscreteChoiceModel(this, parameter)
}

val GlobalRandomizer = Random(1)
