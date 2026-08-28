package benchmarks

import edu.kit.ifv.discretechoice.InvariantArrayChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8D
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.utils.I
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.models.UtilityFunction
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import kotlin.random.Random

class ChoiceModelBenchmark {

}

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




private data class DurationAlternativeEncoded(
    val averageTimeOfActivityCode: Int,
    val dayCode: Int,
    val activityCode: Int,
    val employmentCode: Int,
    val anzaktwieanztagemitzweck: Int,
    val planTimeBudgetsCode: Int,
    val tourPlanSize: Int,
    val tagAmountAlternatives: Int,
    val lastTourOfDayCode: Int,
    val dayHasTwoToursCode: Int,
    val tourPositionCode: Int,
) {
    companion object {
        fun fromAlternative(it: MainDurationAlternative): DurationAlternativeEncoded {
            return DurationAlternativeEncoded(
                averageTimeOfActivityCode = UtilityConverter.convertToFifteenMinuteBlock(
                    it.dayPlan.getBudget(
                        it
                            .activityType
                    )
                ),
                dayCode = UtilityConverter.convertWeekDay(it.dayPlan.durationDay.weekday),
                activityCode = UtilityConverter.convertActivityType(it.activityType),
                employmentCode = UtilityConverter.convertEmployment(it.person.employment),
                anzaktwieanztagemitzweck = it.anzaktwieanztagemitzweck().I,
                planTimeBudgetsCode = it.planTimeBudgets.getCategory(it.activityType).toInt(),
                tourPlanSize = it.tourPlan.size,
                tagAmountAlternatives = it.dayPlan.amountOfActivities,
                lastTourOfDayCode = it.letztetourdestages().I,
                dayHasTwoToursCode = it.taghat2touren().I,
                tourPositionCode = it.tourPlan.position.ordinal,
            )
        }
    }
}
@Volatile
private var blackhole: Int = 0
fun consume(value: Any?, acc: Int): Int =
    acc * 31 + (value?.hashCode() ?: 0)
fun main() {

    val histograms = ArrayHistogram.fromResource(
        identifier = Identifier.MAJOR_ACTIVITY_DURATION
    )

    val build =
        SingularUtilityStructureBuilder<ArrayHistogram, MainDurationAlternative, ParameterStep8D, DurationAlternativeEncoded>(
            histograms.toSet(),
            converter = DurationAlternativeEncoded::fromAlternative,
            parameters = ParametersStep8D.toTypedArray()
        ) { a, it, p ->
            p.run {
                base +

                        when (it.averageTimeOfActivityCode) {

                            0 -> mittl_zeit_akt_1bis14min
                            1 -> mittl_zeit_akt_15bis29min
                            2, 3 -> mittl_zeit_akt_30bis59min
                            4, 5, 6, 7 -> mittl_zeit_akt_60bis119min
                            8, 9, 10, 11 -> mittl_zeit_akt_120bis179min
                            12, 13, 14, 15 -> mittl_zeit_akt_180bis239min
                            else -> .0
                        } +

                        when (it.dayCode) {
                            5 -> tag_fr
                            6 -> tag_sa
                            7 -> tag_so
                            else -> .0
                        } +
                        (it.anzaktwieanztagemitzweck) * anzaktwieanztagemitzweck +
                        when (it.activityCode) {
                            5 -> aktzweck_work
                            0 -> aktzweck_education
                            3 -> aktzweck_shopping
                            4 -> aktzweck_transport
                            else -> .0
                        } +
                        when (it.planTimeBudgetsCode) {
                            1 -> wochenzbudget_zweck_kat1
                            2 -> wochenzbudget_zweck_kat2
                            3 -> wochenzbudget_zweck_kat3
                            4 -> wochenzbudget_zweck_kat4
                            5 -> wochenzbudget_zweck_kat5
                            else -> .0
                        } +
                        when (it.tourPlanSize) {
                            1 -> tourhat1akt
                            2 -> tourhat2akt
                            3 -> tourhat3akt
                            else -> .0
                        } +

                        when (it.tagAmountAlternatives) {
                            2 -> taghat2akt
                            3 -> taghat3akt
                            else -> 0.0
                        } +
                        (it.lastTourOfDayCode) * letztetourdestages +
                        (it.dayHasTwoToursCode) * taghat2touren +
                        when (it.employmentCode) {
                            0 -> beruf_vollzeit
                            1, 7, 8 -> beruf_teilzeit
                            3, 4, 9, 10, 11 -> beruf_schueler_azubi
                            else -> .0
                        } +
                        when (it.tourPositionCode) {
                            0 -> taghat2touren
                            2 -> tourliegtvorhaupttour
                            else -> .0
                        }
            }


        }.build()

    val mobilityPlanInputs = randomMobilityPlanInput()
    val models = AllChoiceModels.DEFAULT
    val major = context(PlanGenerationParameters()) {
        MAJOR
    }
    val b: MainDurationAlternative = MainDurationAlternative(mobilityPlanInputs)
    val warmupRepetitions = 1_000_000
    val repetitions = 20_000_000

    // Warm up both implementations.
    run {
        var acc = 0

        context(Random(42), b) {
            repeat(warmupRepetitions) {
                acc = consume(build.select(), acc)
            }
        }

        context(Random(42), b) {
            repeat(warmupRepetitions) {
                acc = consume(major.selectSingular(b), acc)
            }
        }

        blackhole = acc
    }

    // Implementation 1
    run {
        var acc = 0

        context(Random(42), b) {
            repeat(repetitions) {
                acc = consume(build.select(), acc)
            }
        }

        blackhole = blackhole xor acc
    }

    // Implementation 2
    run {
        var acc = 0

        context(Random(42), b) {
            repeat(repetitions) {
                acc = consume(major.selectSingular(b), acc)
            }
        }

        blackhole = blackhole xor acc
    }

    // Makes the externally observable use completely obvious.
    println("blackhole=$blackhole")


}
