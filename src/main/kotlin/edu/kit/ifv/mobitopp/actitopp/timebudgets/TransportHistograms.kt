package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.TransportBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.TransportBudgetSet
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.TransportBudgets
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitopp.utils.times
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

class TransportHistograms(
    val histogram1: ArrayHistogram,
    val histogram2: ArrayHistogram,
    val histogram3: ArrayHistogram,
    val histogram4: ArrayHistogram,
) : HistogramSelection {

    override fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {
        val converter: (ArrayHistogram) -> WorkChoiceAlternative =
            { WorkChoiceAlternative(it, finalizedActivityPattern, person) }
        return choiceModel.select(random, converter)
    }

    private val choiceModel =
        DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, TransportBudgetSet> {
            option(histogram1) { 0.0 }
            forOptions(histogram2, histogram3, histogram4) { standardUtilityFunction(this, it) }

        }.multinomialLogit("Histogram selection for time budgets for transport").build(TransportBudgets)

    companion object {
        fun fromResourcePath(path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh")): TransportHistograms {
            return TransportHistograms(
                histogram1 = ArrayHistogram.fromPath(path.resolve("7J_KAT_0.csv")),
                histogram2 = ArrayHistogram.fromPath(path.resolve("7J_KAT_1.csv")),
                histogram3 = ArrayHistogram.fromPath(path.resolve("7J_KAT_2.csv")),
                histogram4 = ArrayHistogram.fromPath(path.resolve("7J_KAT_3.csv")),
            )
        }
    }
}


private val standardUtilityFunction: TransportBudgetParameters.(WorkChoiceAlternative) -> Double = {
    base +
            (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
            (it.amountOfShoppingActivitiesInWeek()) * anzakt_woche_s +
            (it.amountOfTransportActivitiesInWeek()) * anzakt_woche_t +
            (it.isFulltimeEmployee()) * beruf_vollzeit +
            (it.isParttimeEmployee()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.amountOfDaysWithTransportActivityIs1()) * tagemit_takt_1

}

