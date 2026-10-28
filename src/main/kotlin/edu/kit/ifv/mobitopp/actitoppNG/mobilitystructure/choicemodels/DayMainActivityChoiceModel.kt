package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.enums.isEmployedAnywhere
import edu.kit.ifv.mobitopp.actitoppNG.enums.isStudentOrAzubi
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.performance.UtilityConverter
import edu.kit.ifv.mobitopp.actitoppNG.utils.D
import edu.kit.ifv.mobitopp.discretechoice.models.ArrayBackedFixedChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.LambdaMapperChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.structure.UtilityEnumerationBuilder
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.EnumeratedDiscreteModelBuilder

context(params: PlanGenerationParameters)
val mainActivityChoiceModel get() =
    DiscreteStructure<ActivityType, DayAlternativeEncoded, DayMainActivitySet> {
        option(ActivityType.WORK, parameters = { this[ActivityType.WORK]!! }, { _, it ->
                (if (it.employment.isEmployedAnywhere() && it.isStandardWorkingDay) 1.3 else 1.0) * standardUtilityFunction(
                    this,
                    it
                )
            }
        )
        option(ActivityType.EDUCATION, parameters = { this[ActivityType.EDUCATION]!! }, { _, it ->
            (if (it.employment.isStudentOrAzubi() && it.isStandardWorkingDay) 1.3 else 1.0) * standardUtilityFunction(
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

    }.multinomialLogitConverted<ActivityType, DayAlternative, DayAlternativeEncoded, DayMainActivitySet>("Main activity type of the day") { dayAlternative ->
        DayAlternativeEncoded(
            dayAlternative
        )
    }.build(params.mainActivityChoiceModelParams)

fun <A, C, X, P> UtilityEnumerationBuilder<A, X, P>.multinomialLogitConverted(
    name: String,
    converter: (C) -> X,
): EnumeratedDiscreteModelBuilder<A, C, P> = OptimizedArrayConversionBuilder(this, name, converter)

class OptimizedArrayConversionBuilder<A, C, X, P>(
    private val utilityBuilder: UtilityEnumerationBuilder<A, X, P>,
    private val name: String,
    val converter: (C) -> X,
): EnumeratedDiscreteModelBuilder<A, C, P> {
    override fun build(parameters: P): FixedChoiceModel<A, C> {
        return utilityBuilder.optimizedMultinomialLogitConversion(name, parameters, converter)
    }
}

fun <A, C, X, P> UtilityEnumerationBuilder<A, X, P>.optimizedMultinomialLogitConversion(
    name: String,
    parameters: P,
    converter: (C) -> X,
): FixedChoiceModel<A, C> {
    val utilityEnumeration = this.build()
    return LambdaMapperChoiceModel(
        ArrayBackedFixedChoiceModel(utilityEnumeration, name = name, parameters = parameters),
        converter
    )
}

data class DayAlternativeEncoded(
    val dayCode: Int,
    val employmentCode: Int,
    val employment: Employment,
    val amountOfYouthsInHousehold: Int,
    val has5WorkDays: Double,
    val has5EduDays: Double,
    val amountWorkingDays: Int,
    val amountEducationDays: Int,
    val amountLeisureDays: Int,
    val amountShoppingDays: Int,
    val amountServiceDays: Int,
    val amountImmobileDays: Int,
    val isStandardWorkingDay: Boolean,
) {
    constructor(alternative: DayAlternative): this(
        dayCode = UtilityConverter.convertWeekDay(alternative.dayOfWeek),
        employmentCode = UtilityConverter.convertEmployment(alternative.employment),
        employment = alternative.employment,
        amountOfYouthsInHousehold = alternative.amountOfYouthsInHousehold,
        has5WorkDays = alternative.has5WorkDays().D,
        has5EduDays = alternative.has5EducationDays().D,
        amountWorkingDays = alternative.amountOfWorkingDays,
        amountEducationDays = alternative.amountOfEducationDays,
        amountLeisureDays = alternative.amountOfLeisureDays,
        amountShoppingDays = alternative.amountOfShoppingDays,
        amountServiceDays = alternative.amountOfServiceDays,
        amountImmobileDays = alternative.amountOfImmobileDays,
        isStandardWorkingDay = alternative.isStandardWorkingDay,
    )
}




private val standardUtilityFunction: DayMainActivityParameters.(DayAlternativeEncoded) -> Double = {
    base +
            when(it.employmentCode) {
                0 -> employmentFullTime
                1, 7, 8 -> employmentPartTime
                2, 5 -> employmentNotEarning
                3, 9, 10, 11 -> employmentStudent
                4 -> employmentVocational
                else -> .0
            } +
            (it.amountOfYouthsInHousehold) * amountOfYouths +
            (it.has5WorkDays) * has5WorkingDays +
            (it.has5EduDays) * has5EducationDays +
            when(it.dayCode) {
                2 -> tuesday
                3 -> wednesday
                4 -> thursday
                5 -> friday
                6 -> saturday
                7 -> sunday
                else -> .0

            } +
            (it.amountWorkingDays) * amountOfWorkdays +
            (it.amountEducationDays) * amountOfEducationDays +
            (it.amountLeisureDays) * amountOfLeisureDays +
            (it.amountShoppingDays) * amountOfShoppingDays +
            (it.amountServiceDays) * amountOfTransportDays +
            (it.amountImmobileDays) * amountOfImmobileDays
}