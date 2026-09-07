package edu.kit.ifv.mobitopp.actitoppNG.timebudgets.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramSelection
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromList

context(params: PlanGenerationParameters)
val workHistograms get() =
    HistogramSelection.createChoiceModelFromResource(
        identifier = Identifier.WORK_TIME_BUDGETS,
        parameter = params.workTimeBudgedParams,
        name = "Selection of histogram for work activities."
    ) { histograms ->
        loadFromList(histograms - histograms[6]) { _,it ->
            base +
                    (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
                    (it.amountOfEducationActivitiesInWeek()) * anzakt_woche_e +
                    (it.isFulltimeEmployee()) * beruf_vollzeit +
                    (it.isParttimeEmployee()) * beruf_teilzeit +
                    (it.isStudent()) * beruf_schueler +
                    (it.isVocational()) * beruf_azubi +
                    (it.isMale()) * male +
                    when (it.age) {
                        in 18..25 -> alter_18bis25
                        in 26..35 -> alter_26bis35
                        in 36..50 -> alter_36bis50
                        in 51..60 -> alter_51bis60
                        else -> 0.0
                    } +
                    (it.amountOfDaysWithWorkActivityIs1()) * tagemit_wakt_1 +
                    (it.amountOfDaysWithWorkActivityIs2()) * tagemit_wakt_2 +
                    (it.amountOfDaysWithWorkActivityIs3()) * tagemit_wakt_3 +
                    (it.amountOfDaysWithWorkActivityIs4()) * tagemit_wakt_4 +
                    (it.amountOfDaysWithWorkActivityIs5()) * tagemit_wakt_5 +
                    (it.amountOfDaysWithWorkActivityIs6()) * tagemit_wakt_6
        }
        option(histograms[6]) {
            0.0
        }
    }




