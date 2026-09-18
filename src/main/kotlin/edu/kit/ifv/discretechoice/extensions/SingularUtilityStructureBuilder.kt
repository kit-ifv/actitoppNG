package edu.kit.ifv.discretechoice.extensions

import edu.kit.ifv.discretechoice.ParameterArrayBackedFixedCM
import edu.kit.ifv.mobitopp.discretechoice.models.LambdaMapperChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.UtilityFunction
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure

/**
 * Convert the complex object C to a more performant X object so that the conversion
 *
 * [utilityFunction] is used to calculate all utilities of
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

    fun build(name: String): LambdaMapperChoiceModel<A, C, X> {
        val assignment = originalBuilder.build()
        val choiceModel = ParameterArrayBackedFixedCM<A, X, P>(
            originalOptions,
            assignment,
            parameters = parameters,
            name = name,
        )
        return LambdaMapperChoiceModel(
            choiceModel,
            converter,
            )
    }
}
