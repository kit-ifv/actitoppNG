package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels

import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DayMainActivityParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DayMainActivitySet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.DefaultDayMainActivityParameters
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.AllocatedLogit
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ModifiableDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.initializeWithParameters
import edu.kit.ifv.mobitopp.actitopp.utils.times

val mainActivityChoiceModel =
    ModifiableDiscreteChoiceModel<ActivityType, DayAlternative, DayMainActivitySet>(AllocatedLogit.create {

        option(ActivityType.WORK, parameters = { work }, {
            (if (it.isEmployedAnywhere() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                this,
                it
            )
        }
        )
        option(ActivityType.EDUCATION, parameters = { education }, {
            (if (it.isStudentOrAzubi() && it.isStandardWorkingDay()) 1.3 else 1.0) * standardUtilityFunction(
                this,
                it
            )
        })
        option(ActivityType.LEISURE, parameters = { leisure }, { standardUtilityFunction(this, it) })
        option(ActivityType.SHOPPING, parameters = { shopping }, { standardUtilityFunction(this, it) })
        option(ActivityType.TRANSPORT, parameters = { transport }, { standardUtilityFunction(this, it) })
        option(ActivityType.HOME) {
            0.0
        }


    }).initializeWithParameters(DefaultDayMainActivityParameters)
private val standardUtilityFunction: DayMainActivityParameters.(DayAlternative) -> Double = {
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