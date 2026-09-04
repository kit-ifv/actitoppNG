package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels


import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8A
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8A
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

context(params: PlanGenerationParameters)
val firstActivityUsesStandardDuration get() =
    DiscreteStructure<Boolean, BooleanDecisionAlternative, ParameterCollectionStep8A> {
        option(true) { 0.0 }
        option(false, parameters = { no }) {_, it ->
            val averageTimeOfActivityCode = UtilityConverter.convertToWeightedMinuteBlock(it.dayPlan.getBudget(it.activityType))
            val numActivitiesTourplan = it.tourPlan.size
            val dayPlanAmountActivities = it.dayPlan.amountOfActivities
            base +
                    (it.aktzweck_work()) * aktzweck_work +
                    (it.aktzweck_education()) * aktzweck_education +
                    (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
                    (it.beruf_vollzeit()) * beruf_vollzeit +
                    (it.beruf_teilzeit()) * beruf_teilzeit +
                    when (dayPlanAmountActivities) {
                        1 -> taghat1akt
                        2 -> taghat2akt
                        3 -> taghat3akt
                        else -> 0.0
                    } +
                    (it.taghat1tour()) * taghat1tour +
                    (it.taghat2touren()) * taghat2touren +
                    (it.tag_sa()) * tag_sa +
                    (it.tag_so()) * tag_so +
                    when (averageTimeOfActivityCode) {
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
                    when (numActivitiesTourplan) {
                        1 -> tourhat1akt
                        2 -> tourhat2akt
                        3 -> tourhat3akt
                        else -> 0.0
                    }
        }

    }.multinomialLogit("If first activity of the day should use a predetermined start time")
        .build(params.firstActivityUsesStandardDurationParams)