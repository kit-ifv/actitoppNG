package edu.kit.ifv.mobitopp.actitopp.weekroutine

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1BWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1CWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1DWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1EWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1FWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1KWithParams
import edu.kit.ifv.mobitopp.actitopp.weekroutine.choicemodels.step1LWithParams

typealias ChoiceModele = EnumeratedDiscreteChoiceModel<Int,PersonAlternative , *>

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
    private val workDayChoiceModel: ChoiceModele = defaultWorkDayChoiceModel,
    private val educationDayChoiceModel: ChoiceModele = step1BWithParams,
    private val leisureDayChoiceModel: ChoiceModele = step1CWithParams,
    private val shoppingDayChoiceModel: ChoiceModele = step1DWithParams,
    private val serviceDayChoiceModel: ChoiceModele = step1EWithParams,
    private val immobileDayChoiceModel: ChoiceModele = step1FWithParams,
    private val averageAmountOfTourChoiceModel: ChoiceModele = step1KWithParams,
    private val averageAmountOfActivitiesChoiceModel: ChoiceModele = step1LWithParams,
) : GenerateWeekRoutine {
    override fun generate(person: IPerson, rng: RNGHelper): WeekRoutine {
        return ModifiableWeekRoutine().apply {

            val converter: (Int) -> PersonAlternative = { PersonAlternative(it, this, person) }
            amountOfWorkingDays = workDayChoiceModel.select(rng, converter)
            amountOfEducationDays = educationDayChoiceModel.select(rng, converter)
            amountOfLeisureDays = leisureDayChoiceModel.select(rng, converter)
            amountOfShoppingDays = shoppingDayChoiceModel.select(rng, converter)
            amountOfServiceDays = serviceDayChoiceModel.select(rng, converter)
            amountOfImmobileDays = immobileDayChoiceModel.select(rng, converter)
            averageAmountOfTours = averageAmountOfTourChoiceModel.select(rng, converter)
            averageAmountOfActivities = averageAmountOfActivitiesChoiceModel.select(rng, converter)
        }
    }
}