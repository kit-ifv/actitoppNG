package edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionWithPreferenceCategory
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterStep10A
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParametersStep10A
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek
import kotlin.random.Random

/**
 * This implementation of [UsePreferredTourStart] utilizes a choice model to evaluate whether a tour should use the
 * preferred histogram to draw the start time. In order to allow the draw from the preferred histogram the following conditions
 * need to be met.
 *
 * 1) The person must be employed or in education.
 * 2) The main activity of the tour must be either [WORK][ActivityType.WORK] or [EDUCATION][ActivityType.EDUCATION]
 *
 * In the legacy implementation there was a deprecated step 3)
 * The person must have an activity of [WORK][ActivityType.WORK] or [EDUCATION][ActivityType.EDUCATION] in their plan.
 *
 * However since condition 2 implies 3, this code has been removed in this implementation.
 */
class PreferredStartViaChoiceModel() : UsePreferredTourStart {
    private val choiceModel =
        DiscreteStructure<Boolean, BooleanDecisionWithPreferenceCategory, ParameterStep10A> {
            option(true) { 0.0 }
            option(false) {_, it ->
                base +
                        (it.anztourenvorhaupttour()) * anztourenvorhaupttour +
                        (it.anztourennachhaupttour()) * anztourennachhaupttour +
                        (it.tourhat1akt()) * tourhat1akt +
                        (it.isAged10to17()) * alter_10bis17 +
                        when(it.tourActivity) {
                            ActivityType.WORK -> tourtyp_work
                            else -> 0.0
                        } +
                        when(it.dayOfWeek) {
                            DayOfWeek.SATURDAY -> tag_sa
                            DayOfWeek.SUNDAY -> tag_so
                            else -> 0.0
                        } +
                        when(it.dauer_akt_in_tour_blocked) {
                            1 -> dauer_akt_in_tour_2bis4std
                            2 -> dauer_akt_in_tour_4bis6std
                            3 -> dauer_akt_in_tour_6bis8std
                            4 -> dauer_akt_in_tour_8bis10std
                            5 -> dauer_akt_in_tour_10bis12std
                            else -> 0.0
                        } +
                        (it.dauer_akt_vorht_tag_1bis120()) * dauer_akt_vorht_tag_1bis120 +
                        (it.std_start_T1_6_7_Uhr()) * std_start_T1_6_7_Uhr +
                        (it.std_start_T1_7_8_Uhr()) * std_start_T1_7_8_Uhr
            }
        }.multinomialLogit("Whether the tour should use a previously determined tour start or not")
            .build(ParametersStep10A)
    context(rng: Random)
    override fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean {
        val relevantActivities = setOf(ActivityType.WORK, ActivityType.EDUCATION)
        if (!input.person.isAnywayEmployed() && !input.person.isinEducation()) return false
        if (input.tourMainActivityType !in relevantActivities) return false
        return context(BooleanDecisionWithPreferenceCategory(input, preferredHistogram)) {
            choiceModel.select()
        }



    }
}