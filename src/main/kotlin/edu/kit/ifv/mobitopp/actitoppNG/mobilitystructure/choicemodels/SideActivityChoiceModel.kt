package edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels

import edu.kit.ifv.discretechoice.extensions.loadOptionsMap
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitoppNG.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitoppNG.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitoppNG.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitoppNG.utils.Position
import edu.kit.ifv.mobitopp.actitoppNG.utils.times
import edu.kit.ifv.mobitopp.discretechoice.structure.DiscreteStructure
import edu.kit.ifv.mobitopp.discretechoice.utilityassignment.multinomialLogit
import java.time.DayOfWeek

context(params: PlanGenerationParameters)
val sideActivityChoiceModel
    get() =
        DiscreteStructure<ActivityType, ActivityAlternativeEncoded, SideActivitySet> {
            loadOptionsMap(listOf(ActivityType.WORK, ActivityType.EDUCATION)) { _, it ->
                standardUtilityFunction(this, it)
            }
            option(ActivityType.LEISURE) { 0.0 }
            loadOptionsMap(listOf(ActivityType.SHOPPING, ActivityType.TRANSPORT)) { _, it ->
                standardUtilityFunction(this, it)
            }
        }.multinomialLogit("Determine Activity type of secondary activities.")
            .build(params.sideActivityChoiceModelParams)

data class ActivityAlternativeEncoded(
    val tourPositionToMain: Position,
    val positionToMainActivity: Position,
    val tourMainActivity: ActivityType,
    val dayOfWeek: DayOfWeek,
    val dayMainActivity: ActivityType,
    val numTourActivities: Int,
    val amountOfWorkingDays: Int,
    val amountOfEducationDays: Int,
    val amountOfShoppingDays: Int,
    val amountOfServiceDays: Int,
) {
    constructor(personWithRoutine: PersonWithRoutine,
                dayStructure: DayStructure,
                tourStructure: BidirectionalIndexedValue<TourStructure>,
                positionToMainActivity: Position,
                plannedTourAmounts: PlannedTourAmounts,
    ) : this(
        tourPositionToMain = tourStructure.position,
        positionToMainActivity = positionToMainActivity,
        dayOfWeek = dayStructure.weekday,
        tourMainActivity = tourStructure.element.mainActivityType(),
        dayMainActivity = dayStructure.mainActivityType(),
        numTourActivities = tourStructure.element.elements().size + plannedTourAmounts.precursorAmount + plannedTourAmounts.successorAmount,
        amountOfWorkingDays = personWithRoutine.routine.amountOfWorkingDays,
        amountOfEducationDays = personWithRoutine.routine.amountOfEducationDays,
        amountOfShoppingDays = personWithRoutine.routine.amountOfShoppingDays,
        amountOfServiceDays = personWithRoutine.routine.amountOfServiceDays,
    )
}

private val standardUtilityFunction: SideActivityParameters.(ActivityAlternativeEncoded) -> Double = {
    base +
            when(it.tourPositionToMain) {
                Position.BEFORE -> tourliegtvorhaupttour
                Position.AFTER -> tourliegtnachhaupttour
                else -> 0.0
            } +
            when(it.positionToMainActivity) {
                Position.BEFORE -> aktliegtvorhauptakt
                else -> 0.0
            } +
            when(it.tourMainActivity) {
                ActivityType.WORK -> tourtyp_work
                ActivityType.EDUCATION -> tourtyp_education
                ActivityType.SHOPPING -> tourtyp_shopping
                ActivityType.TRANSPORT -> tourtyp_transport
                else -> 0.0
            } +
            when(it.dayOfWeek) {
                DayOfWeek.FRIDAY -> tag_fr
                DayOfWeek.SATURDAY -> tag_sa
                DayOfWeek.SUNDAY -> tag_so
                else -> 0.0
            } +
            when(it.dayMainActivity) {
                ActivityType.WORK -> haupttour_work
                ActivityType.EDUCATION -> haupttour_education
                ActivityType.SHOPPING -> haupttour_shopping
                ActivityType.TRANSPORT -> haupttour_transport
                else -> 0.0
            } +
            when(it.numTourActivities) {
                2 -> tourhat2akt
                3 -> tourhat3akt
                4 -> tourhat4akt
                else -> 0.0
            } +
            when(it.amountOfWorkingDays) {
                0 -> anzahl_arbeitstage0
                1 -> anzahl_arbeitstage1
                else -> 0.0
            } +
            when(it.amountOfEducationDays) {
                0 -> anzahl_bildungstage0
                1 -> anzahl_bildungstage1
                else -> 0.0
            } +
            when(it.amountOfShoppingDays) {
                0 -> anzahl_shoppingtage0
                1 -> anzahl_shoppingtage1
                else -> 0.0
            } +
            when(it.amountOfServiceDays) {
                0 -> anzahl_transporttage0
                1 -> anzahl_transporttage1
                else -> 0.0
            }
}