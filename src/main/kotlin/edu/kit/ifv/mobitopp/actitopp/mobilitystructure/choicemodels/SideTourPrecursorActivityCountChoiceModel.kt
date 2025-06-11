package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels

import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DefaultSideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times

import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.TourSituationInt
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters


val step5AWithParams =
    ModifiableDiscreteChoiceModel<Int, TourSituationInt, SideTourPrecursorSet>(AllocatedLogit.create {
        option(0) { 0.0 }
        option(1, parameters = { one }, {
            val util = standardUtilityFunction(this, it)
            util
        })
        option(2, parameters = { two }, { standardUtilityFunction(this, it) })
        option(3, parameters = { three }, { standardUtilityFunction(this, it) })
        option(4, parameters = { four }, { standardUtilityFunction(this, it) })
        option(5, parameters = { five }, { standardUtilityFunction(this, it) })
    }).initializeWithParameters(DefaultSideTourPrecursorParameters)


private val standardUtilityFunction: SideTourPrecursorParameters.(TourSituationInt) -> Double = {
    base +

    (it.isBeforeMainTour()) * tourliegtvorhaupttour+
        (it.isAfterMainTour()) * tourliegtnachhaupttour+
        (it.tourMainActivityIsWork()) * tourtyp_work+
        (it.tourMainActivityIsEducation()) * tourtyp_education+
        (it.tourMainActivityIsShopping()) * tourtyp_shopping+
        (it.tourMainActivityIsTransport()) * tourtyp_transport+
        (it.isSaturday()) * tag_sa+
        (it.isSunday()) * tag_so+
        (it.isAged18To35()) * alter_18bis35+
        (it.isAged36To50()) * alter_36bis50+
        (it.isAged51To60()) * alter_51bis60+
        (it.amountOfToursIs1()) * taghat1tour+
        (it.amountOfToursIs2()) * taghat2touren+
        (it.averageAmountOfActivitiesIs1()) * mean_1akt+
        (it.averageAmountOfActivitiesIs2()) * mean_2akt+
        (it.averageAmountOfActivitiesIs3()) * mean_3akt
}
