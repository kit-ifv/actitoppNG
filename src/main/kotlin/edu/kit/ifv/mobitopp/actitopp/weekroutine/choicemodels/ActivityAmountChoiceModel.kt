package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ActivityAmountParameters
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ActivityAmountSet
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultActivityAmountParameters


val step1LWithParams = ModifiableDiscreteChoiceModel<Int, PersonSituation, ActivityAmountSet>(AllocatedLogit.create {
    option(1) {
        0.0
    }
    option(2, parameters = { option2 }) { standardUtilityFunction(this, it) }
    option(3, parameters = { option3 }) { standardUtilityFunction(this, it) }
    option(4, parameters = { option4 }) { standardUtilityFunction(this, it) }
    option(5, parameters = { option5 }) { standardUtilityFunction(this, it) }
    option(6, parameters = { option6 }) { standardUtilityFunction(this, it) }
}
).initializeWithParameters(DefaultActivityAmountParameters)


private val standardUtilityFunction: ActivityAmountParameters.(PersonSituation) -> Double = {
    base +
            (it.isParttimeEmployee()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.isVocational()) * beruf_azubi +
            (it.isAged26To35()) * alter_26bis35 +
            (it.isAged36To50()) * alter_36bis50 +
            (it.isAged51To60()) * alter_51bis60 +
            (it.isAged61To70()) * alter_61bis70 +
            (it.areaTypeRural()) * Raumtyp_mobitopp_rural +
            (it.commuteOver50km()) * pendeln_ueber50km +
            (it.commuteIn0To5km()) * pendeln_0bis5km +
            (it.hasChildrenInHousehold()) * haushalthatkinderunter10

}
