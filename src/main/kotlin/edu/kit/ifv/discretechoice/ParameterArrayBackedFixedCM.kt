package edu.kit.ifv.discretechoice

import edu.kit.ifv.mobitopp.discretechoice.distribution.CumulateDistributionArray
import edu.kit.ifv.mobitopp.discretechoice.distribution.MultinomialLogitArray
import edu.kit.ifv.mobitopp.discretechoice.distribution.probabilities
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.UtilityFunction
import edu.kit.ifv.mobitopp.discretechoice.selection.SelectionFunctionArray
import edu.kit.ifv.mobitopp.discretechoice.selection.WeightedSelection
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.UtilityAssignment
import kotlin.random.Random

/**
 * The main feature this ChoiceModel has, is the fact that it does not use the same parameter set for every alternative.
 * Instead it uses and array of parameters which map 1:1 to each alternative.
 * Other than that it works just like [edu.kit.ifv.mobitopp.discretechoice.models.ArrayBackedFixedChoiceModel].
 */
class ParameterArrayBackedFixedCM<A, in C, P>(
    override val choices: Set<A>,
    val utilityAssignment: UtilityAssignment<A, C, P>,
    val distributionFunction: CumulateDistributionArray<Unit> = MultinomialLogitArray(),
    val selectionFunction: SelectionFunctionArray = WeightedSelection(),
    val parameters: Array<P>,
    override val name: String,
): FixedChoiceModel<A, C> {
    init {
        require(choices.size == parameters.size) {"A ParameterArrayBackedFixedCM only supports parameter " +
                "arrays of the same size as the number of choices. Each choice is gets one parameter, it is a 1:1 " +
                "mapping. Currently there are ${choices.size} choices but ${parameters.size}"}
    }
    private val alternatives: List<A> = choices.toList()
    private val utilityFunctions: Array<UtilityFunction<A, C, P>> = alternatives.map {
        utilityAssignment.getUtilityFunctionFor(it)!!

    }.toTypedArray()

    private val lookup: Map<A, Int> = alternatives.withIndex().associate { it.value to it.index }

    fun getIndex(alternative: A): Int {
        return lookup[alternative] ?: error("Alternative $alternative is not found in lookup")
    }

    override fun probabilities(utilities: Map<A, Double>): Map<A, Double> {
        val keyMap = utilities.mapKeys { getIndex(it.key) }
        val calculationArray = DoubleArray(alternatives.size) {

            keyMap[it] ?: Double.NEGATIVE_INFINITY
        }
        val probabilities = runCatching { distributionFunction.probabilities(calculationArray, Unit) }.getOrElse {

            DoubleArray(alternatives.size) {
                Double.NEGATIVE_INFINITY
            }
        }

        return utilities.keys.associateWith {
            probabilities[getIndex(it)]
        }
    }

    context(c: C, random: Random)
    override fun select(): A {
        val calculationArray = DoubleArray(choices.size) {
            utilityFunctions[it].calculateUtility(alternatives[it], c, parameters[it])
        }
        return selectInternal(calculationArray) { choices }
    }
    context(random: Random)
    private inline fun selectInternal(array: DoubleArray, fallbackOptions: () -> Collection<A>): A {
        val success = distributionFunction.tryCumulateProbabilities(array, Unit)
        if (!success) {
            return fallbackOptions().random(random)
        }
        val outputIdx = selectionFunction.calculateSelection(
            array, random
        )
        return alternatives[outputIdx]
    }
    context(c: C, random: Random)
    override fun select(choices: Set<A>): A {
        if (choices.size == 1) return choices.iterator().next()
        val calculationArray = spawnUtilityArray(choices, c)
        return selectInternal(calculationArray) {
            choices
        }
    }

    context(c: C, random: Random)
    override fun selectInjected(
        choices: Set<A>,
        injections: Map<A, (Double) -> Double>,
    ): A {
        if (choices.size == 1) return choices.iterator().next()
        val calculationArray = spawnUtilityArray(choices, c)
        injections.forEach { (key, operation) ->
            val idx = getIndex(key)
            calculationArray[idx] = operation(calculationArray[idx])

        }
        return selectInternal(calculationArray) { choices }
    }
    private fun spawnUtilityArray(choices: Set<A>, c: C): DoubleArray {
        val calculationArray = DoubleArray(alternatives.size) {
            Double.NEGATIVE_INFINITY
        }
        choices.forEach {
            val idx = getIndex(it)
            calculationArray[idx] = utilityFunctions[idx].calculateUtility(it, c, parameters[idx])
        }
        return calculationArray
    }
    context(c: C)
    override fun utility(alternative: A): Double {
        val index: Int = getIndex(alternative)
        return utilityFunctions[index].calculateUtility(alternative, c, parameters[index])
    }
}