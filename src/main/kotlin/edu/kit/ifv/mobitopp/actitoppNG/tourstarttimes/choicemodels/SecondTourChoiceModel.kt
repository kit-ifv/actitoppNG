package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.generateHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10O
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import kotlin.time.Duration.Companion.hours


private val standardUtilityFunction10O: ParameterStep10O.(MainDurationAlternative) -> Double = {

    val previousDayTourEndHour = it.dayPlan.endOfPreviousTour(it.tourPlan)
    base +
            (it.beruf_vollzeit()) * beruf_vollzeit +
            (it.beruf_teilzeit()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.isVocational()) * beruf_azubi +
            (it.tourtyp_work()) * tourtyp_work +
            (it.tourtyp_education()) * tourtyp_education +
            (it.tourtyp_shopping()) * tourtyp_shopping +
            (it.tourtyp_transport()) * tourtyp_transport +
            (it.tag_fr()) * tag_fr +
            (it.tag_sa()) * tag_sa +
            (it.tag_so()) * tag_so +
            (it.taghat2akt()) * taghat2akt +
            (it.taghat3akt()) * taghat3akt +
            (it.taghat2touren()) * taghat2touren +
            (it.taghat3touren()) * taghat3touren +
            (it.dauer_akt_in_tour_0bis2std()) * dauer_akt_in_tour_0bis2std +
            (it.dauer_akt_in_tour_2bis4std()) * dauer_akt_in_tour_2bis4std +
            (it.dauer_akt_in_tour_4bis6std()) * dauer_akt_in_tour_4bis6std +
            (it.touristhaupttour()) * touristhaupttour +
            when (previousDayTourEndHour) {
                in 12.hours..<13.hours -> endetourvorher_Std_12
                in 13.hours..<14.hours -> endetourvorher_Std_13
                in 14.hours..<15.hours -> endetourvorher_Std_14
                in 15.hours..<16.hours -> endetourvorher_Std_15
                in 16.hours..<17.hours -> endetourvorher_Std_16
                in 17.hours..<18.hours -> endetourvorher_Std_17
                in 18.hours..<19.hours -> endetourvorher_Std_18
                else -> .0
            }


}

context(params: PlanGenerationParameters)
val SECOND_TOUR_HISTOGRAM
    get() = params.secondTourHistogramParams.generateHistogram(
        ArrayHistogram.fromResource(

            identifier = Identifier.SECOND_TOUR_START_TIME
        ),
        standardUtilityFunction10O
    )