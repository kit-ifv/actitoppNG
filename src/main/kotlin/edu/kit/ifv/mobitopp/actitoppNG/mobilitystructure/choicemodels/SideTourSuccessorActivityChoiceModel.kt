package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels


import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

context(params: PlanGenerationParameters)
val step5BWithParams
    get() =
        DiscreteStructure<Int, TourAlternativeInt, SideTourSuccessorSet> {
            option(0) { 0.0 }
            loadOptionsMap(1..5) { _, it ->
                standardUtilityFunction(this, it)
            }
        }.multinomialLogit("Amount of successor activities in side tours")
            .build(params.step5BWithParamsParams)



private val standardUtilityFunction: SideTourSuccessorParameters.(TourAlternativeInt) -> Double = {
    base +
            (it.isBeforeMainTour()) * tourliegtvorhaupttour +
            (it.isAfterMainTour()) * tourliegtnachhaupttour +
            (it.numActivitiesBeforeMainActivityIs1()) * anzaktvorhauptaktist1 +
            (it.numActivitiesBeforeMainActivityIs2()) * anzaktvorhauptaktist2 +
            (it.numActivitiesBeforeMainActivityIs3()) * anzaktvorhauptaktist3 +
            (it.tourMainActivityIsWork()) * tourtyp_work +
            (it.tourMainActivityIsEducation()) * tourtyp_education +
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
