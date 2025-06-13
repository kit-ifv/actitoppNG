package edu.kit.ifv.mobitopp.actitopp.tourstarttimes

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.BooleanDecisionWithPreferenceCategory
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.parameters.ParameterStep10A
import edu.kit.ifv.mobitopp.actitopp.tourstarttimes.parameters.ParametersStep10A
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times


fun interface UsePreferredTourStart {
    fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean
}


class UseStartViaChoiceModel(private val rngKeeper: RNGHelper) : UsePreferredTourStart {
    private val choiceModel =
        ModifiableDiscreteChoiceModel<Boolean, BooleanDecisionWithPreferenceCategory, ParameterStep10A>(
            AllocatedLogit.create {
                option(true) { 0.0 }
                option(false) {
                    base +
                            (it.anztourenvorhaupttour()) * anztourenvorhaupttour +
                            (it.anztourennachhaupttour()) * anztourennachhaupttour +
                            (it.tourhat1akt()) * tourhat1akt +
                            (it.isAged10to17()) * alter_10bis17 +
                            (it.tourtyp_work()) * tourtyp_work +
                            (it.tag_sa()) * tag_sa +
                            (it.tag_so()) * tag_so +
                            (it.dauer_akt_in_tour_2bis4std()) * dauer_akt_in_tour_2bis4std +
                            (it.dauer_akt_in_tour_4bis6std()) * dauer_akt_in_tour_4bis6std +
                            (it.dauer_akt_in_tour_6bis8std()) * dauer_akt_in_tour_6bis8std +
                            (it.dauer_akt_in_tour_8bis10std()) * dauer_akt_in_tour_8bis10std +
                            (it.dauer_akt_in_tour_10bis12std()) * dauer_akt_in_tour_10bis12std +
                            (it.dauer_akt_vorht_tag_1bis120()) * dauer_akt_vorht_tag_1bis120 +
                            (it.std_start_T1_6_7_Uhr()) * std_start_T1_6_7_Uhr +
                            (it.std_start_T1_7_8_Uhr()) * std_start_T1_7_8_Uhr
                }
            }
        ).initializeWithParameters(ParametersStep10A)

    override fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean {
        if (!input.person.isAnywayEmployed() && !input.person.isinEducation()) return false
        // TODO make this boolean expression more readable via de morgan
        if (input.tourMainActivityType !in setOf(ActivityType.WORK, ActivityType.EDUCATION)) return false
        if (input.mobilityPlan.mainActivityMap[ActivityType.WORK]?.isNotEmpty() == true || input.mobilityPlan.mainActivityMap[ActivityType.EDUCATION]?.isNotEmpty() == true) {
            val converter: (Boolean) -> BooleanDecisionWithPreferenceCategory = {
                BooleanDecisionWithPreferenceCategory(it, input, preferredHistogram)
            }
            val rnd = rngKeeper.randomValue
            return choiceModel.select(rnd, converter)
        }
        return false

    }
}


