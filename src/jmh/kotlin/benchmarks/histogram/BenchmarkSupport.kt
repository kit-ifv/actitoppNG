package benchmarks.histogram

import edu.kit.ifv.discretechoice.InvariantArrayChoiceModel
import edu.kit.ifv.discretechoice.extensions.SingularUtilityStructureBuilder
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8D
import edu.kit.ifv.mobitopp.actitoppNG.randomMobilityPlanInput
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.utils.I
import edu.kit.ifv.mobitopp.discretechoice.models.UtilityFunction
import kotlin.random.Random

/**
 * Shared setup for both:
 *
 *  - the IntelliJ profiler workload
 *  - the JMH benchmark
 *
 * Keep benchmark-specific machinery OUT of this file.
 */

internal data class DurationAlternativeEncoded(
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

        fun fromAlternative(
            alternative: MainDurationAlternative,
        ): DurationAlternativeEncoded {
            return DurationAlternativeEncoded(
                averageTimeOfActivityCode =
                    UtilityConverter.convertToFifteenMinuteBlock(
                        alternative.dayPlan.getBudget(
                            alternative.activityType
                        )
                    ),

                dayCode =
                    UtilityConverter.convertWeekDay(
                        alternative.dayPlan.durationDay.weekday
                    ),

                activityCode = alternative.activityType.ordinal,

                employmentCode =
                    UtilityConverter.convertEmployment(
                        alternative.person.employment
                    ),

                anzaktwieanztagemitzweck =
                    alternative.anzaktwieanztagemitzweck().I,

                planTimeBudgetsCode =
                    alternative.planTimeBudgets
                        .getCategory(alternative.activityType)
                        .toInt(),

                tourPlanSize =
                    alternative.tourPlan.size,

                tagAmountAlternatives =
                    alternative.dayPlan.amountOfActivities,

                lastTourOfDayCode =
                    alternative.letztetourdestages().I,

                dayHasTwoToursCode =
                    (alternative.tag_anzahl_touren == 2).I,

                tourPositionCode =
                    alternative.tourPlan.position.ordinal,
            )
        }
    }
}


/**
 * Just makes the benchmark/profiling code less ridiculous to read.
 */
internal typealias DurationInvariantChoiceModel =
        InvariantArrayChoiceModel<
                ArrayHistogram,
                MainDurationAlternative,
                ParameterStep8D,
                DurationAlternativeEncoded,
                >


/**
 * Everything that both benchmark mechanisms need.
 *
 * MAJOR is deliberately not in here because its concrete type does not
 * need to leak into this shared structure.
 */
internal data class DurationSelectionFixture(
    val invariantModel: DurationInvariantChoiceModel,
    val alternative: MainDurationAlternative,
)


/**
 * The utility function under test.
 *
 * Keeping this outside of createDurationSelectionFixture() makes it
 * considerably easier to inspect and modify without touching the
 * benchmark infrastructure itself.
 */
internal val durationUtilityFunction =
    UtilityFunction<
            ArrayHistogram,
            DurationAlternativeEncoded,
            ParameterStep8D,
            > { _, alternative, parameter ->

        parameter.run {
            base +

                    when (alternative.averageTimeOfActivityCode) {
                        0 ->
                            mittl_zeit_akt_1bis14min

                        1 ->
                            mittl_zeit_akt_15bis29min

                        2, 3 ->
                            mittl_zeit_akt_30bis59min

                        4, 5, 6, 7 ->
                            mittl_zeit_akt_60bis119min

                        8, 9, 10, 11 ->
                            mittl_zeit_akt_120bis179min

                        12, 13, 14, 15 ->
                            mittl_zeit_akt_180bis239min

                        else ->
                            .0
                    } +

                    when (alternative.dayCode) {
                        5 -> tag_fr
                        6 -> tag_sa
                        7 -> tag_so
                        else -> .0
                    } +

                    alternative.anzaktwieanztagemitzweck *
                    anzaktwieanztagemitzweck +

                    when (alternative.activityCode) {
                        5 -> aktzweck_work
                        0 -> aktzweck_education
                        3 -> aktzweck_shopping
                        4 -> aktzweck_transport
                        else -> .0
                    } +

                    when (alternative.planTimeBudgetsCode) {
                        1 -> wochenzbudget_zweck_kat1
                        2 -> wochenzbudget_zweck_kat2
                        3 -> wochenzbudget_zweck_kat3
                        4 -> wochenzbudget_zweck_kat4
                        5 -> wochenzbudget_zweck_kat5
                        else -> .0
                    } +

                    when (alternative.tourPlanSize) {
                        1 -> tourhat1akt
                        2 -> tourhat2akt
                        3 -> tourhat3akt
                        else -> .0
                    } +

                    when (alternative.tagAmountAlternatives) {
                        2 -> taghat2akt
                        3 -> taghat3akt
                        else -> .0
                    } +

                    alternative.lastTourOfDayCode *
                    letztetourdestages +

                    alternative.dayHasTwoToursCode *
                    taghat2touren +

                    when (alternative.employmentCode) {
                        0 ->
                            beruf_vollzeit

                        1, 7, 8 ->
                            beruf_teilzeit

                        3, 4, 9, 10, 11 ->
                            beruf_schueler_azubi

                        else ->
                            .0
                    } +

                    when (alternative.tourPositionCode) {
                        0 -> taghat2touren
                        2 -> tourliegtvorhaupttour
                        else -> .0
                    }
        }
    }


/**
 * Creates the actual workload used by BOTH the profiler and JMH.
 *
 * Anything that changes what is being benchmarked should normally
 * happen here rather than in either benchmark frontend.
 */
internal fun createDurationSelectionFixture(seed: Int = 1234): DurationSelectionFixture {

    val histograms = ArrayHistogram.fromResource(
        identifier = Identifier.MAJOR_ACTIVITY_DURATION
    )

    val invariantModel =
        SingularUtilityStructureBuilder<
                ArrayHistogram,
                MainDurationAlternative,
                ParameterStep8D,
                DurationAlternativeEncoded,
                >(
            originalOptions = histograms.toSet(),
            converter = DurationAlternativeEncoded::fromAlternative,
            parameters = ParametersStep8D.toTypedArray(),
            utilityFunction = durationUtilityFunction,
        ).build()

    val mobilityPlanInputs =
        randomMobilityPlanInput(Random(seed))

    val alternative =
        MainDurationAlternative(mobilityPlanInputs)

    return DurationSelectionFixture(
        invariantModel = invariantModel,
        alternative = alternative,
    )
}