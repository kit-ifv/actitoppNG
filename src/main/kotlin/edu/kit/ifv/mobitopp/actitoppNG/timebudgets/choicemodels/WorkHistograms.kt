package edu.kit.ifv.mobitopp.actitoppNG.timebudgets.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudent
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
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
                    (it.amountOfWorkActivitiesInWeek) * anzakt_woche_w +
                    (it.amountOfEducationActivitiesInWeek) * anzakt_woche_e +
                    when (it.employment) {
                        Employment.FULLTIME -> beruf_vollzeit
                        in isParttime -> beruf_teilzeit
                        Employment.VOCATIONAL -> beruf_azubi
                        in isStudent -> beruf_schueler
                        else -> 0.0
                    } +
                    (it.isMale()) * male +
                    when (it.ageCode) {
                        AgeEncoder.isAged18To25 -> alter_18bis25
                        AgeEncoder.isAged26To35 -> alter_26bis35
                        AgeEncoder.isAged36To50 -> alter_36bis50
                        AgeEncoder.isAged51To60 -> alter_51bis60
                        else -> 0.0
                    } +
                    when(it.amountOfDaysWithWorkActivity) {
                        1 -> tagemit_wakt_1
                        2 -> tagemit_wakt_2
                        3 -> tagemit_wakt_3
                        4 -> tagemit_wakt_4
                        5 -> tagemit_wakt_5
                        6 -> tagemit_wakt_6
                        else -> 0.0
                    }
        }
        option(histograms[6]) {
            0.0
        }
    }




