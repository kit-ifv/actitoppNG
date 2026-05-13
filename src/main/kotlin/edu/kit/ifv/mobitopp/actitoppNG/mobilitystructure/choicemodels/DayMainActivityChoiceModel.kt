package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels


import edu.kit.ifv.discretechoice.extensions.multiAssign
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit

context(params: PlanGenerationParameters)
val mainActivityChoiceModel get() =
    DiscreteStructure<ActivityType, DayAlternative, DayMainActivitySet> {
        option(ActivityType.WORK, parameters = { work }, {_, it ->
                (if (it.isEmployedAnywhere() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                    this,
                    it
                )
            }
        )
        option(ActivityType.EDUCATION, parameters = { education }, {_, it ->
            (if (it.isStudentOrAzubi() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                this,
                it
            )
        })
        multiAssign(
            mapOf(
                ActivityType.LEISURE to { leisure },
                ActivityType.SHOPPING to { shopping },
                ActivityType.TRANSPORT to { transport }
            ),
            utilityFunction = { standardUtilityFunction(this, it.second) }
        )
        option(ActivityType.HOME) {
            0.0
        }

    }.multinomialLogit("Main activity type of the day").build(params.mainActivityChoiceModelParams)



private val standardUtilityFunction: DayMainActivityParameters.( DayAlternative) -> Double = {
    base +
            (it.isFulltimeEmployee()) * employmentFullTime +
            (it.isParttimeEmployee()) * employmentPartTime +
            (it.isNotEarningMoney()) * employmentNotEarning +
            (it.isStudent()) * employmentStudent +
            (it.isVocational()) * employmentVocational +
            (it.amountOfYouthsInHousehold()) * amountOfYouths +
            (it.has5WorkDays()) * has5WorkingDays +
            (it.has5EducationDays()) * has5EducationDays +
            (it.isTuesday()) * tuesday +
            (it.isWednesday()) * wednesday +
            (it.isThursday()) * thursday +
            (it.isFriday()) * friday +
            (it.isSaturday()) * saturday +
            (it.isSunday()) * sunday +
            (it.amountOfWorkingDays()) * amountOfWorkdays +
            (it.amountOfEducationDays()) * amountOfEducationDays +
            (it.amountOfLeisureDays()) * amountOfLeisureDays +
            (it.amountOfShoppingDays()) * amountOfShoppingDays +
            (it.amountOfServiceDays()) * amountOfTransportDays +
            (it.amountOfImmobileDays()) * amountOfImmobileDays
}