package edu.kit.ifv.mobitopp.actitopp.utilityFunctions

import kotlin.math.exp

class Logit<X, P> : DistributionFunction<X, P>, LimitedDistributionFunction<X> {

    override fun calculateProbabilities(evaluators: Map<X, Double>, parameters: P): Map<X, Double> {
        return calculateProbabilities(evaluators)
    }

    override fun calculateProbabilities(evaluators: Map<X, Double>): Map<X, Double> {
        val currentExp = evaluators.entries.associate {
            it.key to
                    exp(it.value)
        }
        val sum = currentExp.values.sum()

        return currentExp.mapValues { it.value / sum }
    }
}


class AllocatedLogit<X : Any, SIT : ChoiceAlternative<X>, P>(
    private val optionsMap: Map<X, UtilityFunction<SIT, P>>,
    override var rules: List<Pair<(SIT) -> Boolean, UtilityFunction<SIT, P>>>,
    override val name: String = "Unnamed allocated logit",
    private val logit: Logit<SIT, P>,

    ) : RuleBasedAssociation<X, SIT, P>, OptionDistributionFunction<X, SIT, P> {
    override val options = optionsMap.keys
    override val translation: Map<X, UtilityFunction<SIT, P>> = optionsMap

    override fun calculateProbabilities(evaluators: Map<SIT, Double>, parameters: P): Map<SIT, Double> {
        return logit.calculateProbabilities(evaluators, parameters)
    }

    override fun translation(target: SIT): UtilityFunction<SIT, P> {
        return super<RuleBasedAssociation>.translation(target)
    }


    companion object {

        private const val DEFAULT_NAME = "Unnamed MNL model"

        class LogitBuilder<X : Any, SIT : ChoiceAlternative<X>, PARAMS>(preknownOptions: Collection<X>) :
            OptionBasedSituationBuilder<X, SIT, PARAMS>, RuleBasedSituationBuilder<X, SIT, PARAMS> {
            val rules: MutableList<Pair<(SIT) -> Boolean, UtilityFunction<SIT, PARAMS>>> = mutableListOf()
            val options: MutableMap<X, UtilityFunction<SIT, PARAMS>> =
                preknownOptions.associateWith { UtilityFunction<SIT, PARAMS> { _, _ -> 0.0 } }
                    .toMutableMap()

            override fun addUtilityFunctionByIdentifier(x: X, utilityFunction: UtilityFunction<SIT, PARAMS>) {
                rules.add({ sit: SIT -> sit.choice == x } to utilityFunction)
                options[x] = utilityFunction
            }

            override fun addUtilityFunctionByRule(
                rule: (SIT) -> Boolean,
                utilityFunction: UtilityFunction<SIT, PARAMS>,
            ) {
                rules.add(rule to utilityFunction)
            }
        }

        fun <X : Any, SIT : ChoiceAlternative<X>, PARAMS> create(
            options: Collection<X>,
            name: String = DEFAULT_NAME,
            lambda: LogitBuilder<X, SIT, PARAMS>.() -> Unit,
        ): AllocatedLogit<X, SIT, PARAMS> {
            val builder = LogitBuilder<X, SIT, PARAMS>(options)
            builder.apply(lambda)

            return AllocatedLogit(builder.options, builder.rules, name = name, Logit())
        }

        fun <X : Any, SIT : ChoiceAlternative<X>, PARAMS> create(
            name: String = DEFAULT_NAME,
            lambda: LogitBuilder<X, SIT, PARAMS>.() -> Unit,
        ): AllocatedLogit<X, SIT, PARAMS> = create(emptySet(), name, lambda)
    }
}


