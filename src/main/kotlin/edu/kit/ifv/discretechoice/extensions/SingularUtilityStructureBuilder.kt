package edu.kit.ifv.discretechoice.extensions

import edu.kit.ifv.discretechoice.InvariantArrayChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.UtilityFunction
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure

/**
 * Convert the complex object C to a more performant X object so that the conversion
 */
class SingularUtilityStructureBuilder<A, C, P, X>(
    private val originalOptions: Set<A>,
    private val converter: (C) -> X,
    private val parameters: Array<P>,
    private val utilityFunction: UtilityFunction<A, X, P>,
) {

    private val originalBuilder: DiscreteStructure<A, X, P> = DiscreteStructure {}

    init {
        originalOptions.forEach {
            originalBuilder.addUtilityFunctionByIdentifier(it, utilityFunction)
        }
    }

    fun build(): InvariantArrayChoiceModel<A, C, P, X> {
        val assignment = originalBuilder.build()
        return InvariantArrayChoiceModel(
            originalOptions,
            assignment,
            converter,
            parameters = this.parameters,

            )
    }
}
