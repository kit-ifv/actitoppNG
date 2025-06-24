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

/**
 * This interface is tasked to determine whether the tour start should use the preferred histogram to select the concrete
 * start time or not. The existence of the preferred histogram is necessary for this evaluation to return a time
 * from said histogram.
 */
fun interface UsePreferredTourStart {
    fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean

    companion object {
        /**
         * The standard [DISABLED] implementation always returns false when determining whether to use the preferred
         * histogram.
         */
        val DISABLED = UsePreferredTourStart { _, _ -> false }
    }
}

/**
 * This implementation of [UsePreferredTourStart] utilizes a choice model to evaluate whether a tour should use the
 * preferred histogram to draw the start time. In order to allow the draw from the preferred histogram the following conditions
 * need to be met.
 *
 * 1) The person must be employed or in education.
 * 2) The main activity of the tour must be either [WORK][ActivityType.WORK] or [EDUCATION][ActivityType.EDUCATION]
 * 3) The person must have an activity of [WORK][ActivityType.WORK] or [EDUCATION][ActivityType.EDUCATION] in their plan.
 */
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
        // This implementation tests against Work and Education as activities with a fixed tour start time
        val relevantActivities = setOf(ActivityType.WORK, ActivityType.EDUCATION)
        if (!input.person.isAnywayEmployed() && !input.person.isinEducation()) return false
        if (input.tourMainActivityType !in relevantActivities) return false
        // If any of the main activities in the mobility plan match the relevant activities, then this code may determine
        // whether the person has a preferred tour start. (Even if the person employment and main activity type mismatch)
        val hasRelevantActivities = input.mobilityPlan.mainActivityMap.run {
            relevantActivities.any {
                getOrDefault(it, emptyList()).isNotEmpty()
            }
        }
        if (hasRelevantActivities) {
            val converter: (Boolean) -> BooleanDecisionWithPreferenceCategory = {
                BooleanDecisionWithPreferenceCategory(it, input, preferredHistogram)
            }
            val rnd = rngKeeper.randomValue
            return choiceModel.select(rnd, converter)
        }
        return false

    }
}


