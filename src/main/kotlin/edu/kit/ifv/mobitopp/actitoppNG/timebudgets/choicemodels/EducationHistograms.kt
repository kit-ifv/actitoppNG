package edu.kit.ifv.mobitopp.actitoppNG.timebudgets.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramSelection
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromList

context(params: PlanGenerationParameters)
val educationHistograms get() =
    HistogramSelection.createChoiceModelFromResource(
        identifier = Identifier.EDUCATION_TIME_BUDGETS,
        parameter = params.educationTimeBudgetParams,
        name = "Histogram selection for education duration"
    ) { histograms ->
        loadFromList(histograms - histograms[3]) {_, it ->


            base +
                    (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
                    (it.amountOfEducationActivitiesInWeek()) * anzakt_woche_e +
                    when (it.age) {
                        in 10..17 -> alter_10bis17
                        else -> 0.0
                    } +
                    when (it.employment) {
                        Employment.VOCATIONAL -> beruf_azubi
                        else -> 0.0
                    } +
                    when(it.amountOfDaysWithEducationActivity) {
                        2 -> tagemit_eakt_2
                        3 -> tagemit_eakt_3
                        4 -> tagemit_eakt_4
                        5 -> tagemit_eakt_5
                        else -> 0.0
                    }
        }
        option(histograms[3]) {
            0.0
        }
}