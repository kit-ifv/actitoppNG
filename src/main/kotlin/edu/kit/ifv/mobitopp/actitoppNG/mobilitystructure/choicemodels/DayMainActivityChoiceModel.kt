package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

context(params: PlanGenerationParameters)
val mainActivityChoiceModel get() =
    DiscreteStructure<ActivityType, DayAlternative, DayMainActivitySet> {
        option(ActivityType.WORK, parameters = { this[ActivityType.WORK]!! }, { _, it ->
                (if (it.isEmployedAnywhere() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                    this,
                    it
                )
            }
        )
        option(ActivityType.EDUCATION, parameters = { this[ActivityType.EDUCATION]!! }, { _, it ->
            (if (it.isStudentOrAzubi() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                this,
                it
            )
        })
        loadOptionsMap(listOf(ActivityType.LEISURE, ActivityType.SHOPPING, ActivityType.TRANSPORT), {
            _, it -> standardUtilityFunction(this, it)
        })
        option(ActivityType.HOME) {
            0.0
        }

    }.multinomialLogit("Main activity type of the day").build(params.mainActivityChoiceModelParams)



private val standardUtilityFunction: DayMainActivityParameters.( DayAlternative) -> Double = {
    val dayCode = UtilityConverter.convertWeekDay(it.dayOfWeek)
    val employmentCode = UtilityConverter.convertEmployment(it.employment)
    base +

            when(employmentCode) {
                0 -> employmentFullTime
                1, 7, 8 -> employmentPartTime
                2, 5 -> employmentNotEarning
                3, 9, 10, 11 -> employmentStudent
                4 -> employmentVocational
                else -> .0
            } +

            (it.amountOfYouthsInHousehold()) * amountOfYouths +
            (it.has5WorkDays()) * has5WorkingDays +
            (it.has5EducationDays()) * has5EducationDays +

            when(dayCode) {
                2 -> tuesday
                3 -> wednesday
                4 -> thursday
                5 -> friday
                6 -> saturday
                7 -> sunday
                else -> .0

            } +
            (it.amountOfWorkingDays()) * amountOfWorkdays +
            (it.amountOfEducationDays()) * amountOfEducationDays +
            (it.amountOfLeisureDays()) * amountOfLeisureDays +
            (it.amountOfShoppingDays()) * amountOfShoppingDays +
            (it.amountOfServiceDays()) * amountOfTransportDays +
            (it.amountOfImmobileDays()) * amountOfImmobileDays
}