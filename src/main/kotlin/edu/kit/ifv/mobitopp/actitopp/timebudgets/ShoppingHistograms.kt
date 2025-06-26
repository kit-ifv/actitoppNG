package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.ShoppingBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.ShoppingBudgetSet
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.ShoppingBudgets
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitopp.utils.times
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

class ShoppingHistograms(
    val histogram1: ArrayHistogram,
    val histogram2: ArrayHistogram,
    val histogram3: ArrayHistogram,
    val histogram4: ArrayHistogram,
    val histogram5: ArrayHistogram,

    ) : HistogramSelection {

    override fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {
        return choiceModel.select(random) { WorkChoiceAlternative(it, finalizedActivityPattern, person) }
    }

    private val choiceModel = DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, ShoppingBudgetSet> {
            option(histogram1, parameters = { category1 }) { standardUtilityFunction(this, it) }
            option(histogram2, parameters = { category2 }) { standardUtilityFunction(this, it) }
            option(histogram3) { 0.0 }
            option(histogram4, parameters = { category4 }) { standardUtilityFunction(this, it) }
            option(histogram5, parameters = { category5 }) { standardUtilityFunction(this, it) }

        }.multinomialLogit("Histogram selection for time budget for shopping").build(ShoppingBudgets)

    companion object {
        fun fromResourcePath(path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh")): ShoppingHistograms {
            return ShoppingHistograms(
                histogram1 = ArrayHistogram.fromPath(path.resolve("7H_KAT_0.csv")),
                histogram2 = ArrayHistogram.fromPath(path.resolve("7H_KAT_1.csv")),
                histogram3 = ArrayHistogram.fromPath(path.resolve("7H_KAT_2.csv")),
                histogram4 = ArrayHistogram.fromPath(path.resolve("7H_KAT_3.csv")),
                histogram5 = ArrayHistogram.fromPath(path.resolve("7H_KAT_4.csv")),
            )
        }
    }
}

private val standardUtilityFunction: ShoppingBudgetParameters.(WorkChoiceAlternative) -> Double = {
    base +
            (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
            (it.amountOfLeisureActivitiesInWeek()) * anzakt_woche_l +
            (it.amountOfShoppingActivitiesInWeek()) * anzakt_woche_s +
            (it.isFulltimeEmployee()) * beruf_vollzeit +
            (it.isParttimeEmployee()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.amountOfDaysWithShoppingActivityIs1()) * tagemit_sakt_1 +
            (it.amountOfDaysWithShoppingActivityIs2()) * tagemit_sakt_2 +
            (it.amountOfDaysWithShoppingActivityIs3()) * tagemit_sakt_3 +
            (it.amountOfDaysWithShoppingActivityIs4()) * tagemit_sakt_4

}

