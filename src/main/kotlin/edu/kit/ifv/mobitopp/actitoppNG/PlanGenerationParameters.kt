package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.EducationBudget
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.EducationBudgetSet
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.LeisureBudgetSet
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.LeisureBudgets
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.ShoppingBudgetSet
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.ShoppingBudgets
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.TransportBudgetSet
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.TransportBudgets
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.WorkBudgetSet
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.parameters.WorkBudgets
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
    // Week routine parameters
    val activityAmountParams: ActivityAmountSet = DefaultActivityAmountParameters,
    val educationDayParams: EducationDaySet = DefaultEducationParameters,
    val homeStayParams: HomeDaySet = DefaultHomeParameters,
    val leisureDaysParams: LeisureDaySet = DefaultLeisureParameters,
    val serviceDayParams: ServiceDaySet = DefaultServiceParameters,
    val shoppingDayParams: ShoppingDaySet = DefaultShoppingParameters,
    val tourAmountParams: TourAmountSet = DefaultTourAmountParameters,
    val workDayParams: WorkDaySet = DefaultWorkParameters,
    // Time budget
    val educationTimeBudgetParams: EducationBudgetSet = EducationBudget,
    val workTimeBudgedParams: WorkBudgetSet = WorkBudgets,
    val leisureTimeBudgetParams: LeisureBudgetSet = LeisureBudgets,
    val shoppingTimeBudgetParams: ShoppingBudgetSet = ShoppingBudgets,
    val transportTimeBudgetParams: TransportBudgetSet = TransportBudgets,



)