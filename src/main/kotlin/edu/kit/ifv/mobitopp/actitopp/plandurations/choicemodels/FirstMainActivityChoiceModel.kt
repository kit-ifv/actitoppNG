package edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels

import discreteChoice.EnumeratedDiscreteChoiceModel
import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitopp.plandurations.parameters.ParameterCollectionStep8A
import edu.kit.ifv.mobitopp.actitopp.plandurations.parameters.ParametersStep8A
import edu.kit.ifv.mobitopp.actitopp.utils.times

val firstActivityUsesStandardDuration: EnumeratedDiscreteChoiceModel<Boolean, BooleanDecisionAlternative, ParameterCollectionStep8A> =
    DiscreteStructure<Boolean, BooleanDecisionAlternative, ParameterCollectionStep8A> {
            option(true) { 0.0 }
            option(false, parameters = { no }) {

                base +
                        (it.aktzweck_work()) * aktzweck_work +
                        (it.aktzweck_education()) * aktzweck_education +
                        (it.anzaktwieanztagemitzweck()) * anzaktwieanztagemitzweck +
                        (it.beruf_vollzeit()) * beruf_vollzeit +
                        (it.beruf_teilzeit()) * beruf_teilzeit +
                        (it.taghat1akt()) * taghat1akt +
                        (it.taghat2akt()) * taghat2akt +
                        (it.taghat3akt()) * taghat3akt +
                        (it.taghat1tour()) * taghat1tour +
                        (it.taghat2touren()) * taghat2touren +
                        (it.tag_sa()) * tag_sa +
                        (it.tag_so()) * tag_so +
                        (it.mittl_zeit_akt_1bis14min()) * mittl_zeit_akt_1bis14min +
                        (it.mittl_zeit_akt_15bis29min()) * mittl_zeit_akt_15bis29min +
                        (it.mittl_zeit_akt_30bis59min()) * mittl_zeit_akt_30bis59min +
                        (it.mittl_zeit_akt_60bis119min()) * mittl_zeit_akt_60bis119min +
                        (it.mittl_zeit_akt_120bis179min()) * mittl_zeit_akt_120bis179min +
                        (it.mittl_zeit_akt_180bis239min()) * mittl_zeit_akt_180bis239min +
                        (it.mittl_zeit_akt_240bis299min()) * mittl_zeit_akt_240bis299min +
                        (it.mittl_zeit_akt_300bis359min()) * mittl_zeit_akt_300bis359min +
                        (it.mittl_zeit_akt_360bis419min()) * mittl_zeit_akt_360bis419min +
                        (it.mittl_zeit_akt_420bis479min()) * mittl_zeit_akt_420bis479min +
                        (it.tourhat1akt()) * tourhat1akt +
                        (it.tourhat2akt()) * tourhat2akt +
                        (it.tourhat3akt()) * tourhat3akt
            }

        }.multinomialLogit("If first activity of the day should use a predetermined start time").build(ParametersStep8A)