package edu.kit.ifv.discretechoice.extensions


import edu.kit.ifv.mobitopp.discretechoice.structure.EnumeratedStructureBuilder
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromList
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromMap

fun <T, A, C, P: List<T>> EnumeratedStructureBuilder<A, C, P>.optionsIndexed(vararg options: A, utilityFunction: T.(A, C) -> Double) {
    loadFromList(options.toList(), utilityFunction)
}

fun <T, A, C, P: List<T>> EnumeratedStructureBuilder<A, C, P>.optionsIndexed(options: Iterable<A>, utilityFunction: T.(A, C) -> Double) {
    loadFromList(options.toList(), utilityFunction)
}

fun <T, A, C, P: Map<A, T>> EnumeratedStructureBuilder<A, C, P>.loadOptionsMap(options: Iterable<A>, utilityFunction: T.(A, C) -> Double) {
    loadFromMap(options.toList(), utilityFunction)
}

/**
 * Assign multiple options the same utility functions but with possibly different parameters.
 * @param optionsParameterMap a map that maps each option to a selector function. The selector function selects
 * something from the parameter-set `T`
 * @param utilityFunction a utility function working on the selected parameter (parameter from the selection function
 * in the options map). It gets a pair of `A` the option and `C` the choice
 */
fun<T, A, C, P> EnumeratedStructureBuilder<A, C, T>.multiAssign(
    optionsParameterMap: Map<A, T.() -> P>,
    utilityFunction: P.(Pair<A, C>) -> Double
) {
    for ((option, parameter) in optionsParameterMap) {
        option(option, parameters = parameter, utilityFunction = utilityFunction)
    }
}