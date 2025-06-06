package edu.kit.ifv.mobitopp.actitopp.steps.step10

import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.steps.step7.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.steps.step8.BooleanDecisionWithPreferenceCategory
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times

fun interface UsePreferredTourStart {
    fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean
}


class UseStartViaChoiceModel(private val rngKeeper: RNGKeeper) : UsePreferredTourStart {
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
        if(!input.person.isAnywayEmployed() && !input.person.isinEducation()) return false
        // TODO make this boolean expression more readable via de morgan
        if(input.tourMainActivityType !in setOf(ActivityType.WORK, ActivityType.EDUCATION)) return false
        if(input.mobilityPlan.mainActivityMap[ActivityType.WORK]?.isNotEmpty() == true || input.mobilityPlan.mainActivityMap[ActivityType.EDUCATION]?.isNotEmpty() == true) {
            val converter: (Boolean) -> BooleanDecisionWithPreferenceCategory = {
                BooleanDecisionWithPreferenceCategory(it, input, preferredHistogram)
            }
            val rnd = rngKeeper.pull("10A")
            return choiceModel.select(rnd, converter)
        }
        return false

    }
}


val ParametersStep10A = ParameterStep10A(
    base = -0.2409,
    anztourenvorhaupttour = -0.3678,
    anztourennachhaupttour = -0.2897,
    tourhat1akt = -0.1876,
    alter_10bis17 = -0.8706,
    tourtyp_work = -0.3522,
    tag_sa = 1.4061,
    tag_so = 1.7576,
    dauer_akt_in_tour_2bis4std = 0.3190,
    dauer_akt_in_tour_4bis6std = -0.5283,
    dauer_akt_in_tour_6bis8std = -0.5120,
    dauer_akt_in_tour_8bis10std = -0.9989,
    dauer_akt_in_tour_10bis12std = -1.0021,
    dauer_akt_vorht_tag_1bis120 = 0.8132,
    std_start_T1_6_7_Uhr = -0.1871,
    std_start_T1_7_8_Uhr = -0.3214
)

data class ParameterStep10A(
    val base: Double,
    val anztourenvorhaupttour: Double,
    val anztourennachhaupttour: Double,
    val tourhat1akt: Double,
    val alter_10bis17: Double,
    val tourtyp_work: Double,
    val tag_sa: Double,
    val tag_so: Double,
    val dauer_akt_in_tour_2bis4std: Double,
    val dauer_akt_in_tour_4bis6std: Double,
    val dauer_akt_in_tour_6bis8std: Double,
    val dauer_akt_in_tour_8bis10std: Double,
    val dauer_akt_in_tour_10bis12std: Double,
    val dauer_akt_vorht_tag_1bis120: Double,
    val std_start_T1_6_7_Uhr: Double,
    val std_start_T1_7_8_Uhr: Double,

    )