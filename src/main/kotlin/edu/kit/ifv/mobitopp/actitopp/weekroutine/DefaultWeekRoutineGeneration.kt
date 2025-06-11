package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.steps.PersonSituation
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.SealedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1BWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1CWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1DWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1EWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1FWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1KWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1LWithParams

typealias ChoiceModel = SealedDiscreteChoiceModel<Int, PersonSituation>
/**
 * The standard implementation to generate a week routine. Using choice models and mutable fields. Note that the
 * execution order is important, because the choice models take into account the selection of preceding decisions.
 *
 */
class DefaultWeekRoutineGeneration(
    private val workDayChoiceModel: ChoiceModel = defaultWorkDayChoiceModel,
    private val educationDayChoiceModel: ChoiceModel = step1BWithParams,
    private val leisureDayChoiceModel: ChoiceModel = step1CWithParams,
    private val shoppingDayChoiceModel: ChoiceModel = step1DWithParams,
    private val serviceDayChoiceModel: ChoiceModel = step1EWithParams,
    private val immobileDayChoiceModel: ChoiceModel = step1FWithParams,
    private val averageAmountOfTourChoiceModel: ChoiceModel = step1KWithParams,
    private val averageAmountOfActivitiesChoiceModel: ChoiceModel = step1LWithParams,
) : GenerateWeekRoutine {
    override fun generate(person: IPerson, rng: RNGHelper): WeekRoutine {
        return ModifiableWeekRoutine().run {

            val converter: (Int) -> PersonSituation = { PersonSituation(it, this, person) }
            amountOfWorkingDays = workDayChoiceModel.select(rng.randomValue, converter)
            amountOfEducationDays = educationDayChoiceModel.select(rng.randomValue, converter)
            amountOfLeisureDays = leisureDayChoiceModel.select(rng.randomValue, converter)
            amountOfShoppingDays = shoppingDayChoiceModel.select(rng.randomValue, converter)
            amountOfServiceDays = serviceDayChoiceModel.select(rng.randomValue, converter)
            amountOfImmobileDays = immobileDayChoiceModel.select(rng.randomValue, converter)
            averageAmountOfTours = averageAmountOfTourChoiceModel.select(rng.randomValue, converter)
            averageAmountOfActivities = averageAmountOfActivitiesChoiceModel.select(rng.randomValue, converter)
            toWeekRoutine()
        }
    }
}