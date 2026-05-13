package edu.kit.ifv.discretechoice.extensions

import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
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
 * Asign multiple options the same utility function.
 */
fun<PARAMETER, OPTION, PARAMETER_SET, CHOICE> DiscreteStructure<OPTION, CHOICE, PARAMETER_SET>.multiAssign(
    optionsParameterMap: Map<OPTION, PARAMETER_SET.() -> PARAMETER>,
    utilityFunction: PARAMETER.(Pair<OPTION, CHOICE>) -> Double
) {
    for ((option, parameter) in optionsParameterMap) {
        option(option, parameters = parameter, utilityFunction = utilityFunction)
    }
}