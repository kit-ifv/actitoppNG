package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels


import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.multinomialLogitConverted
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8A
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import java.time.DayOfWeek

/**
 * Parameter object specifically for [firstActivityUsesStandardDuration] choicemodel. Extracts the average time of
 * activity calculation out of the utility function.
 */
data class FirstActivityParameters(
    val averageTimeOfActivityCode: Int,
    val original:  BooleanDecisionAlternative,
) {
    constructor(params: BooleanDecisionAlternative) : this(
        averageTimeOfActivityCode = UtilityConverter.convertToWeightedMinuteBlock(params.dayPlan.getBudget(params.activityType)),
        original = params,
    )
}

context(params: PlanGenerationParameters)
val firstActivityUsesStandardDuration get() =
    DiscreteStructure<Boolean, FirstActivityParameters, ParameterCollectionStep8A> {
        option(true) { 0.0 }
        option(false, parameters = { no }) {_, it ->
            base +
                    when (it.original.activityType) {
                        ActivityType.WORK -> aktzweck_work
                        ActivityType.EDUCATION -> aktzweck_education
                        else -> 0.0
                    } +
                    (it.original.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
                    when (it.original.person.employment) {
                        Employment.FULLTIME -> beruf_vollzeit
                        Employment.PARTTIME, Employment.UNKNOWN_21, Employment.MARGINAL -> beruf_teilzeit
                        else -> 0.0
                    } +
                    when (it.original.dayPlan.amountOfActivities) {
                        1 -> taghat1akt
                        2 -> taghat2akt
                        3 -> taghat3akt
                        else -> 0.0
                    } +
                    when (it.original.dayPlan.tourPlans.size) {
                        1 -> taghat1tour
                        2 -> taghat2touren
                        else -> 0.0
                    } +
                    when (it.original.dayPlan.durationDay.weekday) {
                        DayOfWeek.SATURDAY -> tag_sa
                        DayOfWeek.SUNDAY -> tag_so
                        else -> 0.0
                    } +
                    when (it.averageTimeOfActivityCode) {
                        0 -> mittl_zeit_akt_1bis14min
                        1 -> mittl_zeit_akt_15bis29min
                        2 -> mittl_zeit_akt_30bis59min
                        3 -> mittl_zeit_akt_60bis119min
                        4 -> mittl_zeit_akt_120bis179min
                        5 -> mittl_zeit_akt_180bis239min
                        6 -> mittl_zeit_akt_240bis299min
                        7 -> mittl_zeit_akt_300bis359min
                        8 -> mittl_zeit_akt_360bis419min
                        9 -> mittl_zeit_akt_420bis479min
                        else -> .0
                    } +
                    when (it.original.tourPlan.size) {
                        1 -> tourhat1akt
                        2 -> tourhat2akt
                        3 -> tourhat3akt
                        else -> 0.0
                    }
        }
    }.multinomialLogitConverted("If first activity of the day should use a predetermined start time",
        ::FirstActivityParameters)
        .build(params.firstActivityUsesStandardDurationParams)