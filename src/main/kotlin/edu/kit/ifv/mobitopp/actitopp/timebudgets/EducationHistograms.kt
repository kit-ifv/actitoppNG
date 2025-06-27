package edu.kit.ifv.mobitopp.actitopp.timebudgets

import discreteChoice.structure.DiscreteStructure
import discreteChoice.utility.multinomialLogit
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudget
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudgetSet
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.forOptions
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitopp.utils.times
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.random.Random

@Serializable
class EducationHistograms(
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

    @Transient
    private val choiceModel =
        DiscreteStructure<ArrayHistogram, WorkChoiceAlternative, EducationBudgetSet> {
            forOptions(histogram1, histogram2, histogram3, histogram5, histogram6) {
                standardUtilityFunction(this, it)
            }
            option(histogram4) { 0.0 }
        }.multinomialLogit("Histogram selection for education duration").build(EducationBudget)


    companion object {
        fun fromResourcePath(path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh")): EducationHistograms {
            return EducationHistograms(
                histogram1 = ArrayHistogram.fromPath(path.resolve("7D_KAT_0.csv")),
                histogram2 = ArrayHistogram.fromPath(path.resolve("7D_KAT_1.csv")),
                histogram3 = ArrayHistogram.fromPath(path.resolve("7D_KAT_2.csv")),
                histogram4 = ArrayHistogram.fromPath(path.resolve("7D_KAT_3.csv")),
                histogram5 = ArrayHistogram.fromPath(path.resolve("7D_KAT_4.csv")),
                histogram6 = ArrayHistogram.fromPath(path.resolve("7D_KAT_5.csv"))
            )
        }
    }
}

private val standardUtilityFunction: EducationBudgetParameters.(WorkChoiceAlternative) -> Double = {
    base +
            (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
            (it.amountOfEducationActivitiesInWeek()) * anzakt_woche_e +
            (it.isAged10To17()) * alter_10bis17 +
            (it.isVocational()) * beruf_azubi +
            (it.amountOfDaysWithEducationActivityIs2()) * tagemit_eakt_2 +
            (it.amountOfDaysWithEducationActivityIs3()) * tagemit_eakt_3 +
            (it.amountOfDaysWithEducationActivityIs4()) * tagemit_eakt_4 +
            (it.amountOfDaysWithEducationActivityIs5()) * tagemit_eakt_5

}

