package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.EnumeratedDiscreteChoiceModel
import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import org.jetbrains.annotations.TestOnly
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

open class HistogramSelection(
    val choiceModel: EnumeratedDiscreteChoiceModel<ArrayHistogram, WorkChoiceAlternative, *>,
) {

    val choices = choiceModel.choices
    val converter: (ArrayHistogram, FinalizedActivityPattern, IPerson) -> WorkChoiceAlternative =
        { a, f, p ->
            WorkChoiceAlternative(a, f, p) }
    fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {

        return choiceModel.select(random) {
            converter(it, finalizedActivityPattern, person)
        }

    }

    @TestOnly
    fun utilities(        finalizedActivityPattern: FinalizedActivityPattern,
                          person: IPerson,): Map<WorkChoiceAlternative, Double> {
        return choiceModel.model.utilities(choices.map { converter(it, finalizedActivityPattern, person) }.toSet())
    }

    companion object {
        fun <P> createChoiceModel(
            parameter: P,
            name: String,
            structure: DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, P>.() -> Unit,
        ): HistogramSelection {
            val choiceModel = DiscreteStructure(structure).multinomialLogit(name).build(parameter)
            return HistogramSelection(choiceModel)
        }

        /**
         * Creates the histogram selection by parsing all files that match the specified identifier as given by [Identifier]
         * Afterwards, the parameter object is linked with the
         */
        fun <P> createChoiceModelFromFiles(
            path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh"),
            identifier: Identifier,
            parameter: P,
            name: String,
            structure: DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, P>.(List<ArrayHistogram>) -> Unit,
        ): HistogramSelection {

            val inputs = ArrayHistogram.fromFolder(path, identifier)


            val lambda: DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, P>.() -> Unit = {
                structure(this, inputs)
            }
            val choiceModel = DiscreteStructure(lambda).multinomialLogit(name).build(parameter)
            require(choiceModel.choices.containsAll(inputs)) {
                "The choice model [${choiceModel.name}] has not defined a utility function for ${inputs.toSet().subtract(choiceModel.choices)} \n" +
                        "since this is most likely undesired behaviour the program will terminate."
            }
            return HistogramSelection(choiceModel)
        }
    }
}

