package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels


import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.Employment
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.PrecursorTourAmountParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.performance.AgeEncoder
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import java.time.DayOfWeek

context(params: PlanGenerationParameters)
val precursorAmountChoiceModel get() =
    DiscreteStructure<Int, PreviousDayAlternativeEncodedRaw, PrecursorTourAmountSet> {
        option(0) { 0.0 }
        loadOptionsMap(1..5){ _, it -> standardUtilityFunction(this, it)}
    }.multinomialLogitConverted<Int, PreviousDayAlternative, PreviousDayAlternativeEncodedRaw, PrecursorTourAmountSet>(
        "Amount of precursor tours (tours before main tour) per day",
        converter = { PreviousDayAlternativeEncodedRaw(it) }
    ).build(params.precursorAmountChoiceModelParams)

data class PreviousDayAlternativeEncodedRaw(
    val isFulltimeEmployee: Boolean,
    val dayMainActivity: ActivityType,
    val weekDay: DayOfWeek,
    val isMale: Boolean,
    val ageCode: Int,
    val commuteIn0To5km: Boolean,
    val averageAmountOfToursIs1: Boolean,
    val averageAmountOfToursIs2: Boolean,
    val previousDayHasNoBeforeTour: Boolean,
    val previousDayHasOneBeforeTour: Boolean,
) {
    constructor(raw: PreviousDayAlternative) : this(
        isFulltimeEmployee = raw.employment == Employment.FULLTIME,
        dayMainActivity = raw.dayMainActivity,
        weekDay = raw.dayOfWeek,
        isMale = raw.isMale(),
        ageCode = raw.ageCode,
        commuteIn0To5km = raw.commuteIn0To5km(),
        averageAmountOfToursIs1 = raw.averageAmountOfToursIs1(),
        averageAmountOfToursIs2 = raw.averageAmountOfToursIs2(),
        previousDayHasNoBeforeTour = raw.previousDayHasNoBeforeTour(),
        previousDayHasOneBeforeTour = raw.previousDayHasOneBeforeTour(),
    )
}

private val standardUtilityFunction: PrecursorTourAmountParameters.(PreviousDayAlternativeEncodedRaw) -> Double = {
    base +
            (it.isFulltimeEmployee) * employmentFullTime +
            when(it.dayMainActivity) {
                ActivityType.WORK -> mainActivityIsWork
                ActivityType.EDUCATION -> mainActivityIsEducation
                ActivityType.SHOPPING -> mainActivityIsShopping
                else -> 0.0
            } +
            when(it.weekDay) {
                DayOfWeek.SATURDAY -> saturday
                DayOfWeek.SUNDAY -> sunday
                else -> 0.0
            } +
            (it.isMale) * male +
            when(it.ageCode) {
                AgeEncoder.isAged10To17 -> age10To17
                AgeEncoder.isAged26To35 -> aged26To35
                AgeEncoder.isAged36To50 -> aged36To50
                AgeEncoder.isAged51To60 -> aged51To60
                else -> 0.0
            } +
            (it.commuteIn0To5km) * commuteIn0To5km +
            (it.averageAmountOfToursIs1) * averageAmountOfToursIs1 +
            (it.averageAmountOfToursIs2) * averageAmountOfToursIs2 +
            (it.previousDayHasNoBeforeTour) * previousDayHas0TourBeforeMainAct +
            (it.previousDayHasOneBeforeTour) * previousDayHas1TourBeforeMainAct
}