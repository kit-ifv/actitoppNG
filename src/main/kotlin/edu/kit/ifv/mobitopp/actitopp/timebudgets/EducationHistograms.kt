package edu.kit.ifv.mobitopp.actitopp.timebudgets

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudget
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.EducationBudgetSet
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.file.Path
import kotlin.io.path.Path

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
        randomNumber: Double,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {
        return choiceModel.select(randomNumber) { WorkChoiceSituation(it, finalizedActivityPattern, person) }
    }

    @Transient
    private val choiceModel =
        ModifiableDiscreteChoiceModel<ArrayHistogram, WorkChoiceSituation, EducationBudgetSet>(AllocatedLogit.create {
            option(histogram1, parameters = { category1 }) { standardUtilityFunction(this, it) }
            option(histogram2, parameters = { category2 }) { standardUtilityFunction(this, it) }
            option(histogram3, parameters = { category3 }) { standardUtilityFunction(this, it) }
            option(histogram4) { 0.0 }
            option(histogram5, parameters = { category5 }) { standardUtilityFunction(this, it) }
            option(histogram6, parameters = { category6 }) { standardUtilityFunction(this, it) }
        }).initializeWithParameters(EducationBudget)


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

private val standardUtilityFunction: EducationBudgetParameters.(WorkChoiceSituation) -> Double = {
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

