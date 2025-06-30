package edu.kit.ifv.mobitopp.actitoppNG.weekroutine

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.Person
import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utilityFunctions.select
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.activityAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.educationDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.homeDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.leisureDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.serviceDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.shoppingDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.tourAmountChoiceModel

typealias ChoiceModele = EnumeratedDiscreteChoiceModel<Int, PersonAlternative, *>

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
    private val educationDayChoiceModel: ChoiceModele = educationDaysChoiceModel,
    private val leisureDayChoiceModel: ChoiceModele = leisureDaysChoiceModel,
    private val shoppingDayChoiceModel: ChoiceModele = shoppingDaysChoiceModel,
    private val serviceDayChoiceModel: ChoiceModele = serviceDaysChoiceModel,
    private val immobileDayChoiceModel: ChoiceModele = homeDaysChoiceModel,
    private val averageAmountOfTourChoiceModel: ChoiceModele = tourAmountChoiceModel,
    private val averageAmountOfActivitiesChoiceModel: ChoiceModele = activityAmountChoiceModel,
) : GenerateWeekRoutine {
    override fun generate(person: Person, rng: RNGHelper): WeekRoutine {
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