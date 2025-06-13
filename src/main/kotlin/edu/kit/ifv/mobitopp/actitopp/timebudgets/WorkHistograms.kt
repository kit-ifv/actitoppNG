package edu.kit.ifv.mobitopp.actitopp.timebudgets

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAttributesFromElement
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.WorkBudgetParameters
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.WorkBudgetSet
import edu.kit.ifv.mobitopp.actitopp.timebudgets.parameters.WorkBudgets
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ChoiceSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.times
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Basically the same as step 7B, but reasonable
 */
class WorkHistograms(
    val histogram1: ArrayHistogram,
    val histogram2: ArrayHistogram,
    val histogram3: ArrayHistogram,
    val histogram4: ArrayHistogram,
    val histogram5: ArrayHistogram,
    val histogram6: ArrayHistogram,
    val histogram7: ArrayHistogram,
    val histogram8: ArrayHistogram,
    val histogram9: ArrayHistogram,

    ) : HistogramSelection {

    override fun select(
        randomNumber: Double,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram {
        return choiceModel.select(randomNumber) { WorkChoiceSituation(it, finalizedActivityPattern, person) }
    }

    private val choiceModel = ModifiableDiscreteChoiceModel<ArrayHistogram, WorkChoiceSituation, WorkBudgetSet>(
        AllocatedLogit.create {
            option(histogram1, parameters = { category1 }) { standardUtilityFunction(this, it) }
            option(histogram2, parameters = { category2 }) { standardUtilityFunction(this, it) }
            option(histogram3, parameters = { category3 }) { standardUtilityFunction(this, it) }
            option(histogram4, parameters = { category4 }) { standardUtilityFunction(this, it) }
            option(histogram5, parameters = { category5 }) { standardUtilityFunction(this, it) }
            option(histogram6, parameters = { category6 }) { standardUtilityFunction(this, it) }
            option(histogram7) { 0.0 }
            option(histogram8, parameters = { category8 }) { standardUtilityFunction(this, it) }
            option(histogram9, parameters = { category9 }) { standardUtilityFunction(this, it) }

        }
    ).initializeWithParameters(WorkBudgets)

    companion object {
        fun fromResourcePath(path: Path = Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh")): WorkHistograms {
            return WorkHistograms(
                histogram1 = ArrayHistogram.fromPath(path.resolve("7B_KAT_0.csv")),
                histogram2 = ArrayHistogram.fromPath(path.resolve("7B_KAT_1.csv")),
                histogram3 = ArrayHistogram.fromPath(path.resolve("7B_KAT_2.csv")),
                histogram4 = ArrayHistogram.fromPath(path.resolve("7B_KAT_3.csv")),
                histogram5 = ArrayHistogram.fromPath(path.resolve("7B_KAT_4.csv")),
                histogram6 = ArrayHistogram.fromPath(path.resolve("7B_KAT_5.csv")),
                histogram7 = ArrayHistogram.fromPath(path.resolve("7B_KAT_6.csv")),
                histogram8 = ArrayHistogram.fromPath(path.resolve("7B_KAT_7.csv")),
                histogram9 = ArrayHistogram.fromPath(path.resolve("7B_KAT_8.csv"))
            )
        }
    }
}

private val standardUtilityFunction: WorkBudgetParameters.(WorkChoiceSituation) -> Double = {
    base +
            (it.amountOfWorkActivitiesInWeek()) * anzakt_woche_w +
            (it.amountOfEducationActivitiesInWeek()) * anzakt_woche_e +
            (it.isFulltimeEmployee()) * beruf_vollzeit +
            (it.isParttimeEmployee()) * beruf_teilzeit +
            (it.isStudent()) * beruf_schueler +
            (it.isVocational()) * beruf_azubi +
            (it.isMale()) * male +
            (it.isAged18To25()) * alter_18bis25 +
            (it.isAged26To35()) * alter_26bis35 +
            (it.isAged36To50()) * alter_36bis50 +
            (it.isAged51To60()) * alter_51bis60 +
            (it.amountOfDaysWithWorkActivityIs1()) * tagemit_wakt_1 +
            (it.amountOfDaysWithWorkActivityIs2()) * tagemit_wakt_2 +
            (it.amountOfDaysWithWorkActivityIs3()) * tagemit_wakt_3 +
            (it.amountOfDaysWithWorkActivityIs4()) * tagemit_wakt_4 +
            (it.amountOfDaysWithWorkActivityIs5()) * tagemit_wakt_5 +
            (it.amountOfDaysWithWorkActivityIs6()) * tagemit_wakt_6
}


class WorkChoiceSituation private constructor(
    override val choice: ArrayHistogram,
    val personAttributes: PersonAttributes,
    val patternAttributes: FinalizedPatternAttributes,
) :
    ChoiceSituation<ArrayHistogram>(), PersonAttributes by personAttributes,
    FinalizedPatternAttributes by patternAttributes {
    constructor(choice: ArrayHistogram, finalizedPattern: FinalizedActivityPattern, person: IPerson) : this(
        choice,
        PersonAttributesFromElement(person),
        PatternAttributesByElement(finalizedPattern)
    )

}