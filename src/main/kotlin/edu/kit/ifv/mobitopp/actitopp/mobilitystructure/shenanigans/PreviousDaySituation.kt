package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans


import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.DurationDay
import edu.kit.ifv.mobitopp.actitopp.modernization.ModifiablePlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.steps.DayAttributesFromStructure
import edu.kit.ifv.mobitopp.actitopp.steps.DayStructureAttributes
import edu.kit.ifv.mobitopp.actitopp.steps.PartialTourLayoutAttributes
import edu.kit.ifv.mobitopp.actitopp.utilityFunctions.ChoiceSituation


class PlannedTourMap(
    private val mapping: Map<DurationDay, ModifiablePlannedTourAmounts>,
) {
    constructor(initialDayStructures: Collection<DayStructure>) : this(
        initialDayStructures.associate { it.startTimeDay to ModifiablePlannedTourAmounts() }
    )

    fun getPreviousPlannedTourAmounts(day: DayStructure): PlannedTourAmounts? {
        val lowerEntry = mapping[day.startTimeDay.previous()]
        return lowerEntry
    }

    fun getModifiablePlannedTourAmounts(day: DayStructure): ModifiablePlannedTourAmounts {
        return mapping.getOrElse(day.startTimeDay) { throw NoSuchElementException("There is no day in $mapping") }
    }

    operator fun get(day: DayStructure): ModifiablePlannedTourAmounts? {
        return mapping[day.startTimeDay]
    }

    fun getCurrentPlannedPrecursorTours(day: DayStructure) = getModifiablePlannedTourAmounts(day).precursorAmount

    fun readOnly(): Map<DurationDay, PlannedTourAmounts> = mapping
}

class PreviousDaySituation private constructor(
    override val choice: Int,
    val previousDayAttributes: PreviousDayAttributes,
    val pAttr: PersonAndRoutineAttributes,
    val plannedTourAttributes: PartialTourLayoutAttributes,
    val structureAttributes: DayStructureAttributes,
) : ChoiceSituation<Int>(), PreviousDayAttributes by previousDayAttributes,
    PersonAndRoutineAttributes by pAttr, PartialTourLayoutAttributes by plannedTourAttributes,
    DayStructureAttributes by structureAttributes {


    constructor(
        choice: Int,
        day: DayStructure,
        previousResults: PlannedTourAmounts?,
        personWithRoutine: PersonWithRoutine,
        plannedPrecursorTours: Int,
    ) : this(
        choice = choice,
        previousDayAttributes = PreviousDayAttributesNumeric(
            dayStructure = day,
            plannedTourAmounts = previousResults
        ),
        pAttr = PersonAndRoutineFrom(personWithRoutine),
        plannedTourAttributes = PartialTourLayoutAttributes { plannedPrecursorTours },
        structureAttributes = DayAttributesFromStructure(day),
    )


}