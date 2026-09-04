package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.enums.AreaType
import edu.kit.ifv.mobitopp.actitoppNG.enums.isParttime
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek

data class PreviousDayAlternativeEncoded(
    val dayMainActivityCode: Byte, // 1 -> Work, 2 -> Education, 3 -> Shopping
    val isParttimeEmployee: Boolean,
    val dayOfWeek: DayOfWeek,
    val ageCode: Byte, // 1 -> 26To35, 2 -> 36To50, 3 -> 51To60
    val areaType: AreaType,
    val amountOfPkw: Byte,
    val amountOfBeforeTours: Byte,
    val commuteLengthCode: Byte, // 1 -> 0..5km, 2 -> >50km
    val previousDayNumAftertour: Byte,
    val averageAmountOfTours: Int
) {
    constructor(dayTourPlan: PlannedTourAmounts,
                previousTourPlan: PlannedTourAmounts?,
                day: DayStructure,
                personWithRoutine: PersonWithRoutine) : this(
                    dayMainActivityCode = when (day.mainActivityType()) {
                        ActivityType.WORK -> 1
                        ActivityType.EDUCATION -> 2
                        ActivityType.SHOPPING -> 3
                        else -> -1
                    },
                    isParttimeEmployee = personWithRoutine.person.employment.isParttime(),
                    dayOfWeek = day.weekday,
                    ageCode = personWithRoutine.person.age.let {
                        if (it < 26) -1
                        else if( it < 36) 1
                        else if( it < 51) 2
                        else if (it < 61) 3
                        else -1
                    },
                    areaType = personWithRoutine.person.household.areaType,
                    amountOfPkw = personWithRoutine.person.household.numberOfCars.let {
                        if (it in Byte.MIN_VALUE..Byte.MAX_VALUE) it.toByte()
                        else -1
                    },
                    amountOfBeforeTours = dayTourPlan.precursorAmount.let {
                        if (it in Byte.MIN_VALUE..Byte.MAX_VALUE) it.toByte()
                        else -1
                    },
                    commuteLengthCode = personWithRoutine.person.maxCommute.let {
                        if (it < 0) -1
                        else if( it <= 5.0) 1
                        else if (it > 50) 2
                        else -1
                    },
                    previousDayNumAftertour = previousTourPlan?.successorAmount.let {
                        if (it in Byte.MIN_VALUE..Byte.MAX_VALUE) it?.toByte() ?: -1
                        else -1
                    },
                    averageAmountOfTours = personWithRoutine.routine.averageAmountOfTours,
                )
}

context(params: PlanGenerationParameters)
val successorAmountChoiceModel
    get() =
        DiscreteStructure<Int, PreviousDayAlternativeEncoded, SuccessorTourAmountSet> {
            option(0) { 0.0 }
            loadOptionsMap(1..5) { _, it ->
                standardUtilityFunction_(this, it)
            }
        }.multinomialLogit("Amount of successor tours (tours after main tour)")
            .build(params.successorAmountChoiceModelParams)

private val standardUtilityFunction_: SuccessorTourAmountParameters.(PreviousDayAlternativeEncoded) -> Double = {
    base +
            it.isParttimeEmployee * employmentPartTime +
            when (it.dayMainActivityCode) {
                1.toByte() -> mainActivityIsWork
                2.toByte() -> mainActivityIsEducation
                3.toByte() -> mainActivityIsShopping
                else -> 0.0
            } +
            when (it.dayOfWeek) {
                DayOfWeek.FRIDAY -> friday
                DayOfWeek.SATURDAY -> saturday
                DayOfWeek.SUNDAY -> sunday
                else -> 0.0
            } +
            when(it.ageCode) {
                1.toByte() -> aged26To35
                2.toByte() -> aged36To50
                3.toByte() -> aged51To60
                else -> 0.0
            } +
            when(it.areaType) {
                AreaType.CONURBATION -> areaTypeConurbation
                AreaType.RURAL -> areaTypeRural
                else -> 0.0
            } +
            (it.amountOfPkw) * amountOfPKW +
            (it.amountOfBeforeTours) * amountOfToursBeforeMainAct +
            when(it.commuteLengthCode){
                1.toByte() -> commuteIn0To5km
                2.toByte() -> commuteOver50km
                else -> 0.0
            } +
            when (it.averageAmountOfTours) {
                1 -> averageAmountOfToursIs1
                2 -> averageAmountOfToursIs2
                else -> 0.0
            } +
            when(it.previousDayNumAftertour) {
                0.toByte() -> previousDayHas0TourAfterMainAct
                1.toByte() -> previousDayHas1TourAfterMainAct
                else -> 0.0
            }
}

private val standardUtilityFunction: SuccessorTourAmountParameters.(PreviousDayAlternative) -> Double = {
    base +
            (it.isParttimeEmployee()) * employmentPartTime +
            (it.dayMainActivityIsWork()) * mainActivityIsWork +
            (it.dayMainActivityIsEducation()) * mainActivityIsEducation +
            (it.dayMainActivityIsShopping()) * mainActivityIsShopping +
            when (it.dayOfWeek) {
                DayOfWeek.FRIDAY -> friday
                DayOfWeek.SATURDAY -> saturday
                DayOfWeek.SUNDAY -> sunday
                else -> 0.0
            } +
            (it.isAged26To35()) * aged26To35 +
            (it.isAged36To50()) * aged36To50 +
            (it.isAged51To60()) * aged51To60 +
            (it.areaTypeConurbation()) * areaTypeConurbation +
            (it.areaTypeRural()) * areaTypeRural +
            (it.amountOfPKW()) * amountOfPKW +
            (it.amountOfBeforeTours()) * amountOfToursBeforeMainAct +
            (it.commuteOver50km()) * commuteOver50km +
            (it.commuteIn0To5km()) * commuteIn0To5km +
            (it.averageAmountOfToursIs1()) * averageAmountOfToursIs1 +
            (it.averageAmountOfToursIs2()) * averageAmountOfToursIs2 +
            (it.previousDayHasNoAfterTour()) * previousDayHas0TourAfterMainAct +
            (it.previousDayHasOneAfterTour()) * previousDayHas1TourAfterMainAct
}
