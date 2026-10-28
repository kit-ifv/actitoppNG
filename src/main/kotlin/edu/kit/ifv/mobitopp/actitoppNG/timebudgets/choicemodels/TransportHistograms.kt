package edu.kit.ifv.mobitopp.actitoppNG.timebudgets.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramSelection
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.loadFromList

context(params: PlanGenerationParameters)
val transportHistograms get() = HistogramSelection.createChoiceModelFromResource(
    identifier = Identifier.TRANSPORT_TIME_BUDGETS,
    parameter = params.transportTimeBudgetParams,
    name = "Histogram selection for time budgets for transport"
) { histograms ->
    option(histograms[0]) {
        0.0
    }
    loadFromList(histograms - histograms[0]) { _, it->
        base +
                (it.amountOfWorkActivitiesInWeek) * anzakt_woche_w +
                (it.amountOfShoppingActivitiesInWeek) * anzakt_woche_s +
                (it.amountOfTransportActivitiesInWeek) * anzakt_woche_t +
                when (it.employment) {
                    Employment.FULLTIME -> beruf_vollzeit
                    in isParttime -> beruf_teilzeit
                    in isStudent -> beruf_schueler
                    else -> 0.0
                } +
                if(it.amountOfDaysWithTransportActivityIs1) tagemit_takt_1 else 0.0
    }

}

