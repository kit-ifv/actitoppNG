package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.EnumeratedDiscreteChoiceModel
import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.Person
import edu.kit.ifv.mobitopp.actitopp.plandurations.Identifier
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

open class HistogramSelection(
    val choiceModel: EnumeratedDiscreteChoiceModel<ArrayHistogram, WorkChoiceAlternative, *>,
) {

    val choices = choiceModel.choices
    val converter: (ArrayHistogram, FinalizedActivityPattern, Person) -> WorkChoiceAlternative =
        { a, f, p ->
            WorkChoiceAlternative(a, f, p)
        }

    /**
     * Select a histogram from the options registered in the choice model, for a given [FinalizedActivityPattern] and
     * [Person] input.
     */
    fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: Person,
    ): ArrayHistogram {

        return choiceModel.select(random) {
            converter(it, finalizedActivityPattern, person)
        }

    }


    companion object {


        /**
         * Creates the histogram selection by parsing all files that match the specified identifier as given by [Identifier]
         * The construction of the choice model takes into account the parameter object and the parsed inputs, which are
         * accessible during the creation of the structure as anonymous parameter. To verify that the [structure] parameter
         * handles all parsed inputs the method checks whether each of the inputs has been assigned a utility function
         * in the builder.
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
                "The choice model [${choiceModel.name}] has not defined a utility function for ${
                    inputs.toSet().subtract(choiceModel.choices)
                } \n" +
                        "since this is most likely undesired behaviour the program will terminate."
            }
            return HistogramSelection(choiceModel)
        }
    }
}

