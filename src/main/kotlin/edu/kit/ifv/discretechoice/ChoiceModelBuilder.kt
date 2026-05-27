package edu.kit.ifv.discretechoice

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit


typealias UtilityFunction<C, P> = P.(C) -> Double
class MultiChoiceModelBuilder<A, C, P>(
    private val name: String,
    val utilityFunction: UtilityFunction<C, P>,
) {
    fun <X> build(parameters: X, lambda: DiscreteStructure<A, C, X>.(UtilityFunction<C, P>) -> Unit):
            FixedChoiceModel<A, C> {
        val helper = BuilderHelper(utilityFunction)
        return DiscreteStructure(helper.build(lambda)).multinomialLogit(name).build(parameters)
    }



}

class BuilderHelper<C, P>(val utilityFunction: UtilityFunction<C, P>) {

    fun <A, X> build(lambda: DiscreteStructure<A, C, X>.(UtilityFunction<C, P>) -> Unit): DiscreteStructure<A, C, X>.()
    -> Unit {
        return {
            lambda(utilityFunction)
        }
    }
}


private val standardUtilityFunction: SideTourPrecursorParameters.(TourAlternativeInt) -> Double = {
    base +

            (it.isBeforeMainTour()) * tourliegtvorhaupttour +
            (it.isAfterMainTour()) * tourliegtnachhaupttour +
            (it.tourMainActivityIsWork()) * tourtyp_work +
            (it.tourMainActivityIsEducation()) * tourtyp_education +
            (it.tourMainActivityIsShopping()) * tourtyp_shopping +
            (it.tourMainActivityIsTransport()) * tourtyp_transport +
            (it.isSaturday()) * tag_sa +
            (it.isSunday()) * tag_so +
            (it.isAged18To35()) * alter_18bis35 +
            (it.isAged36To50()) * alter_36bis50 +
            (it.isAged51To60()) * alter_51bis60 +
            (it.amountOfToursIs1()) * taghat1tour +
            (it.amountOfToursIs2()) * taghat2touren +
            (it.averageAmountOfActivitiesIs1()) * mean_1akt +
            (it.averageAmountOfActivitiesIs2()) * mean_2akt +
            (it.averageAmountOfActivitiesIs3()) * mean_3akt
}

val step5ATemp = MultiChoiceModelBuilder<Int, TourAlternativeInt, SideTourPrecursorParameters>(
    name = "Amount of precursor Activities in side tours",
    utilityFunction = standardUtilityFunction,
)

//fun create() {
//    step5ATemp.build(DefaultSideTourPrecursorParameters) { util ->
//        option(0) {0.0}
//        option(1, parameters = {one}, {x -> util(this, x.second)})
//    }
//}

class Advanced<A, C,X, P: Map<A, X>>(
    private val name: String,
    private val parameterMap: P,
    private val utilityFunction: X.(A, C) -> Double,
) {
    fun build(appendix: DiscreteStructure<A, C, P>.() -> Unit ): FixedChoiceModel<A, C> {
        return DiscreteStructure {
            loadOptionsMap(parameterMap.keys, utilityFunction)
            appendix()
        }.multinomialLogit(name).build(parameterMap)
    }
}