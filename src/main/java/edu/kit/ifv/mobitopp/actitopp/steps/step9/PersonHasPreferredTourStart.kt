package edu.kit.ifv.mobitopp.actitopp.steps.step9

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.steps.step10.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitopp.steps.step7.ArrayHistogram
import edu.kit.ifv.mobitopp.actitopp.steps.step8.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.steps.step8.PlanSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times
import kotlin.time.Duration

interface PersonPreferredTourStart {

    fun determinePreferredTourStart(input: MobilityPlanInputs): ArrayHistogram
}

class TourStartSituation(override val choice: Duration, input: MobilityPlanInputs) :
    PlanSituation<Duration>(choice, input) {

}


private val standardUtilityFunction9A: ParameterStep9A.(MainDurationSituation) -> Double = {
    base +
            (it.isStudent()) * beruf_schueler +
            (it.householdHasYouths()) * haushalthatkinderunter18 +
            (it.isAged10to17()) * alter_10bis17 +
            (it.anztagemit_tourenvorht()) * anztagemit_tourenvorht +
            (it.anztagemit_tourennachht()) * anztagemit_tourennachht

}

val ParametersStep9A = ParameterCollectionStep9A(
    emptyList()
)

data class ParameterCollectionStep9A(
    val parameters: List<ParameterStep9A>,
) {
    operator fun get(index: Int) = parameters[index]
}

data class ParameterStep9A(
    val base: Double,
    val beruf_schueler: Double,
    val haushalthatkinderunter18: Double,
    val alter_10bis17: Double,
    val anztagemit_tourenvorht: Double,
    val anztagemit_tourennachht: Double,

    )


class StandardPreferredTourStart(private val rng: RNGHelper) : PersonPreferredTourStart {
    private val choiceModel = ModifiableDiscreteChoiceModel<ArrayHistogram, MainDurationSituation, ParameterCollectionStep9A>(
        AllocatedLogit.create {

            bulk(FIRST_TOUR_HISTOGRAM.histograms.withIndex().associate { (index, histogram) -> histogram to { p: ParameterCollectionStep9A -> p[index]} }){ standardUtilityFunction9A(this, it) }
        }
    ).initializeWithParameters(ParametersStep9A)

    override fun determinePreferredTourStart(input: MobilityPlanInputs): ArrayHistogram {
        return choiceModel.select(rng.randomValue) { MainDurationSituation(it, input) }
    }
}
