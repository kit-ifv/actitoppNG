package edu.kit.ifv.mobitopp.actitopp.weekroutine

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.steps.DayActivityTracker
import org.jetbrains.annotations.TestOnly
import kotlin.properties.Delegates

class ModifiableWeekPattern(val original: IPerson) {
    var amountOfWorkingDays: Int by Delegates.notNull()
    var amountOfEducationDays: Int by Delegates.notNull()
    var amountOfLeisureDays: Int by Delegates.notNull()
    var amountOfShoppingDays: Int by Delegates.notNull()
    var amountOfServiceDays: Int by Delegates.notNull()
    var amountOfImmobileDays: Int by Delegates.notNull()
    var averageAmountOfTours: Int by Delegates.notNull()
    var averageAmountOfActivities: Int by Delegates.notNull()

    fun toWeekRoutine(): WeekRoutine {
        return WeekRoutineImpl(
            amountOfWorkingDays = amountOfWorkingDays,
            amountOfEducationDays = amountOfEducationDays,
            amountOfLeisureDays = amountOfLeisureDays,
            amountOfShoppingDays = amountOfShoppingDays,
            amountOfServiceDays = amountOfServiceDays,
            amountOfImmobileDays = amountOfImmobileDays,
            averageAmountOfTours = averageAmountOfTours,
            averageAmountOfActivities = averageAmountOfActivities
        )
    }
}
fun IPerson.linkModifiableWeekPattern() = ModifiableWeekPattern(this)

interface WeekRoutine {
    val amountOfWorkingDays: Int
    val amountOfEducationDays: Int
    val amountOfLeisureDays: Int
    val amountOfShoppingDays: Int
    val amountOfServiceDays: Int
    val amountOfImmobileDays: Int
    val averageAmountOfTours: Int
    val averageAmountOfActivities: Int
}

data class WeekRoutineImpl(
    override val amountOfWorkingDays: Int,
    override val amountOfEducationDays: Int,
    override val amountOfLeisureDays: Int,
    override val amountOfShoppingDays: Int,
    override val amountOfServiceDays: Int,
    override val amountOfImmobileDays: Int,
    override val averageAmountOfTours: Int,
    override val averageAmountOfActivities: Int,
): WeekRoutine {



    /**
     * To enable testing, we need to be able to load the person attributes based on the Person Week Routine.
     */
    @TestOnly
    // TODO this is only required for testing against the legacy code base, once established this can be removed.
    fun loadToAttributeMap(attributeMap: MutableMap<String, Double>) {
        attributeMap["anztage_w"] = amountOfWorkingDays.toDouble()
        attributeMap["anztage_e"] = amountOfEducationDays.toDouble()
        attributeMap["anztage_l"] = amountOfLeisureDays.toDouble()
        attributeMap["anztage_s"] = amountOfShoppingDays.toDouble()
        attributeMap["anztage_t"] = amountOfServiceDays.toDouble()
        attributeMap["anztage_immobil"] = amountOfImmobileDays.toDouble()
        attributeMap["anztourentag_mean"] = averageAmountOfTours.toDouble()
        attributeMap["anzakttag_mean"] = averageAmountOfActivities.toDouble()
    }
}



