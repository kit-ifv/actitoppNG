package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.SealedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1BWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1CWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1DWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1EWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1FWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1KWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1LWithParams

typealias ChoiceModel = SealedDiscreteChoiceModel<Int, PersonAlternative>

/**
 * The standard implementation to generate a week routine. Using choice models and mutable fields. Note that the
 * execution order is important, because the choice models take into account the selection of preceding decisions.
 *
 * This implementation uses choice models to generate the amount of days in the following order:
 * Work, Education, Leisure, Shopping, Service, Home, Average amount of tours, Average amount of activities.
 *
 * Since the underlying choice models require the incomplete state of the [WeekRoutine] for the utility calculation (for example the
 * amount of work days influence the decision of the amount of education days) We utilize the [ModifiableWeekRoutine]
 * subclass where the fields can be manipulated, so that the incomplete information can be passed on to the subsequent
 * choice model steps.
 *
 * Note that this is an implementation detail for these particular choice models and the [GenerateWeekRoutine] interface
 * will return a read-only [WeekRoutine] as final output.
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
        return ModifiableWeekRoutine().apply {

            val converter: (Int) -> PersonAlternative = { PersonAlternative(it, this, person) }
            amountOfWorkingDays = workDayChoiceModel.select(rng.randomValue, converter)
            amountOfEducationDays = educationDayChoiceModel.select(rng.randomValue, converter)
            amountOfLeisureDays = leisureDayChoiceModel.select(rng.randomValue, converter)
            amountOfShoppingDays = shoppingDayChoiceModel.select(rng.randomValue, converter)
            amountOfServiceDays = serviceDayChoiceModel.select(rng.randomValue, converter)
            amountOfImmobileDays = immobileDayChoiceModel.select(rng.randomValue, converter)
            averageAmountOfTours = averageAmountOfTourChoiceModel.select(rng.randomValue, converter)
            averageAmountOfActivities = averageAmountOfActivitiesChoiceModel.select(rng.randomValue, converter)
        }
    }
}