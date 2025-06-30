package edu.kit.ifv.mobitopp.actitopp.timebudgets.choicemodels

import edu.kit.ifv.mobitopp.actitopp.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitopp.timebudgets.HistogramSelection
import edu.kit.ifv.mobitopp.actitopp.timebudgets.WorkChoiceAlternative
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.LeisureBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.LeisureBudgets
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times


val leisureHistograms by lazy {
    HistogramSelection.createChoiceModelFromFiles(
        identifier = Identifier.LEISURE_TIME_BUDGETS,
        parameter = LeisureBudgets,
        name = "Histogram selection for time budget for leisure"
    ) { histograms ->
        forOptions(histograms - histograms[3]) {
            base +
                    (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
                    (it.amountOfLeisureActivitiesInWeek()) * anzakt_woche_l +
                    (it.amountOfShoppingActivitiesInWeek()) * anzakt_woche_s +
                    (it.commuteIn0To5km()) * pendeln_0bis5km +
                    (it.isRetired()) * beruf_rentner +
                    (it.amountOfDaysWithLeisureActivityIs1()) * tagemit_lakt_1 +
                    (it.amountOfDaysWithLeisureActivityIs2()) * tagemit_lakt_2 +
                    (it.amountOfDaysWithLeisureActivityIs3()) * tagemit_lakt_3 +
                    (it.amountOfDaysWithLeisureActivityIs4()) * tagemit_lakt_4 +
                    (it.amountOfDaysWithLeisureActivityIs5()) * tagemit_lakt_5
        }
        option(histograms[3]) {
            0.0
        }


    }
}

