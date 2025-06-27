package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.LeisureBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.LeisureBudgetSet
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.LeisureBudgets
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitopp.utils.times
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

class LeisureHistograms(
    val histogram1: ArrayHistogram,
    val histogram2: ArrayHistogram,
    val histogram3: ArrayHistogram,
    val histogram4: ArrayHistogram,
    val histogram5: ArrayHistogram,
    val histogram6: ArrayHistogram,
) : HistogramSelection {

    override fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {
        return choiceModel.select(random) { WorkChoiceAlternative(it, finalizedActivityPattern, person) }
    }


    private val choiceModel = DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, LeisureBudgetSet> {
        forOptions(histogram1,
            histogram2,
            histogram3,
            histogram5,
            histogram6,) {
            standardUtilityFunction(this, it)
        }
        option(histogram4) { 0.0 }

    }.multinomialLogit("Histogram selection for time budget for leisure").build(LeisureBudgets)

    companion object {
        fun fromResourcePath(path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh")): LeisureHistograms {
            return LeisureHistograms(
                histogram1 = ArrayHistogram.fromPath(path.resolve("7F_KAT_0.csv")),
                histogram2 = ArrayHistogram.fromPath(path.resolve("7F_KAT_1.csv")),
                histogram3 = ArrayHistogram.fromPath(path.resolve("7F_KAT_2.csv")),
                histogram4 = ArrayHistogram.fromPath(path.resolve("7F_KAT_3.csv")),
                histogram5 = ArrayHistogram.fromPath(path.resolve("7F_KAT_4.csv")),
                histogram6 = ArrayHistogram.fromPath(path.resolve("7F_KAT_5.csv"))
            )
        }
    }

}

private val standardUtilityFunction: LeisureBudgetParameters.(WorkChoiceAlternative) -> Double = {
    base +
            (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
            (it.amountOfLeisureActivitiesInWeek()) * anzakt_woche_l +
            (it.amountOfShoppingActivitiesInWeek()) * anzakt_woche_s +
            (it.commuteIn0To5km()) * pendeln_0bis5km +
            (it.isRetired()) * beruf_rentner +
            (it.amountOfDaysWithLeisureActivityIs1()) * tagemit_lakt_1 +
            (it.amountOfDaysWithLeisureActivityIs2()) * tagemit_lakt_2 +
            (it.amountOfDaysWithLeisureActivityIs3()) * tagemit_lakt_3 +
            (it.amountOfDaysWithLeisureActivityIs4()) * tagemit_lakt_4 +
            (it.amountOfDaysWithLeisureActivityIs5()) * tagemit_lakt_5
}

