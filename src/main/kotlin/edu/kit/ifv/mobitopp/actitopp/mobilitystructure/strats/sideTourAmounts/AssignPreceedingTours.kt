package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.sideTourAmounts

import edu.kit.ifv.mobitopp.actitopp.ActitoppPerson
import edu.kit.ifv.mobitopp.actitopp.HDay
import edu.kit.ifv.mobitopp.actitopp.HTour
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiableDayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.modernization.MutableTourStructure
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import kotlin.math.sign

data class PrecedingInput(
    val personInfo: PersonWithRoutine,
    val day: DayStructure,
    val currentPlannedTourAmounts: PlannedTourAmounts,
    val previousPlannedTourAmounts: PlannedTourAmounts?,

    ) {
//    constructor(input: PersonWithRoutine, ints: Collection<Int>, days: List<DayStructure>): this(input, ints.zip(days).map{DayWithBounds(it.second, it.first)})
}

data class DayWithBounds(
    val day: DayStructure,
    val lowerBoundFromJointAction: Int,
    val amountOfPredecessorTours: Int = 1  // In step 3A we can safely assume that there be only 1 tour, the main tour
)

fun interface GenerateSideTours {
    fun generate(precedingInput: PrecedingInput): Int

}


//fun ActitoppPerson.generatePrecedingTours(weekRoutine: WeekRoutine, lowerBoundsFromJointActions: Collection<Int>, lambda: ActitoppPerson.() -> GenerateSideTours): List<Pair<Int, HDay>> {
//    val strategy = this.lambda()
//    val input = PrecedingInput(
//        PersonWithRoutine(this, weekRoutine),
//        lowerBoundsFromJ ointActions,
//        weekPattern.days.map { it.toStructure() }
//    )
//    return strategy.generate(input).zip(weekPattern.days)
//
//}
fun HDay.toStructure(): DayStructure {
    val structure = ModifiableDayStructure(weekday.value, MutableTourStructure(mainTourType))
    this.tours.forEach {
        when(it.index.sign){
            -1 -> structure.addPrecursor(it.toStructure())
            1 -> structure.addSuccessor(it.toStructure())
        }
    }
    return structure
}

fun HTour.toStructure(): MutableTourStructure {
    val tourStructure = MutableTourStructure(mainActivity()!!.activityType)
    this.activities.forEach {
        when(it.index.sign) {
            -1 -> tourStructure.addPrecursor(it.activityType)
            1 -> tourStructure.addSuccessor(it.activityType)
        }
    }
    return tourStructure
}
fun ActitoppPerson.assignPrecedingTours(activitiesPerDay: List<Pair<Int, HDay>>) {
    activitiesPerDay.forEach { (amount, day) ->
        repeat(amount) {
            day.generatePrecedingTour()
        }
    }
}

