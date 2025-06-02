package edu.kit.ifv.mobitopp.actitopp.steps.step10

import edu.kit.ifv.mobitopp.actitopp.steps.step8.Identifier
import edu.kit.ifv.mobitopp.actitopp.steps.step8.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.steps.step8.durationHistogramsFromResourcePath
import edu.kit.ifv.mobitopp.actitopp.steps.step8.generateHistogram
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times


private val standardUtilityFunction10S: ParameterStep10S.(MainDurationSituation) -> Double = {
    base +

            (it.touristhaupttour()) * touristhaupttour+
                (it.anztourenamtag()) * anztourenamtag+
                (it.tour3destages()) * tour3destages+
                (it.dauer_akt_tag_4bis6std()) * dauer_akt_tag_4bis6std+
                (it.dauer_akt_tag_6bis8std()) * dauer_akt_tag_6bis8std+
                (it.dauer_akt_tag_8bis10std()) * dauer_akt_tag_8bis10std+
                (it.dauer_akt_tag_10bis12std()) * dauer_akt_tag_10bis12std+
                (it.dauer_akt_tag_12bis14std()) * dauer_akt_tag_12bis14std+
                (it.endetourvorher_Std_12()) * endetourvorher_Std_12+
                (it.endetourvorher_Std_13()) * endetourvorher_Std_13+
                (it.endetourvorher_Std_14()) * endetourvorher_Std_14+
                (it.endetourvorher_Std_15()) * endetourvorher_Std_15+
                (it.endetourvorher_Std_16()) * endetourvorher_Std_16+
                (it.endetourvorher_Std_17()) * endetourvorher_Std_17+
                (it.endetourvorher_Std_18()) * endetourvorher_Std_18+
                (it.endetourvorher_Std_19()) * endetourvorher_Std_19+
                (it.endetourvorher_Std_20()) * endetourvorher_Std_20+
                (it.isStudent()) * beruf_schueler

}

val OTHER_TOUR_HISTOGRAM = ParametersStep10S.generateHistogram(
    durationHistogramsFromResourcePath(Identifier.OTHER_TOUR_START_TIME),
    standardUtilityFunction10S
) {
        index, obj ->
    {obj.parameters[index]}
}