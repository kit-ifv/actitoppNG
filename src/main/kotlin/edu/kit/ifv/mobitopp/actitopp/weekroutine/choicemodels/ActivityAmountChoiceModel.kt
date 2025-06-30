package edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utils.times
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.ActivityAmountSet
import edu.kit.ifv.mobitopp.actitopp.weekroutine.parameters.DefaultActivityAmountParameters

/**
 * The choice model for selecting the average amount of activities per day in the week routine. The default option of
 * 1 has associated utility 0.0. The other remaining options between 2 to 6 share a common utility function.
 */
val activityAmountChoiceModel = DiscreteStructure<Int, PersonAlternative, ActivityAmountSet> {
    option(1) {
        0.0
    }
    forOptions(2..6) {
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
}.multinomialLogit("Amount of Activities in Week Routine").build(DefaultActivityAmountParameters)