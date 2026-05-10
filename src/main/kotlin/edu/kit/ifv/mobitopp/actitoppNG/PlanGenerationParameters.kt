package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.ActivityAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultActivityAmountParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultEducationParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultHomeParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultLeisureParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultServiceParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultShoppingParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultTourAmountParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.DefaultWorkParameters
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.EducationDaySet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.HomeDaySet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.LeisureDaySet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.ServiceDaySet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.ShoppingDaySet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.TourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.parameters.WorkDaySet

/**
 * A set of all choice model parameters needed for a household plan generation.
 */
data class PlanGenerationParameters(
    val activityAmountParams: ActivityAmountSet = DefaultActivityAmountParameters,
    val educationDayParams: EducationDaySet = DefaultEducationParameters,
    val homeStayParams: HomeDaySet = DefaultHomeParameters,
    val leisureDaysParams: LeisureDaySet = DefaultLeisureParameters,
    val serviceDayParams: ServiceDaySet = DefaultServiceParameters,
    val shoppingDayParams: ShoppingDaySet = DefaultShoppingParameters,
    val tourAmountParams: TourAmountSet = DefaultTourAmountParameters,
    val workDayParams: WorkDaySet = DefaultWorkParameters
)