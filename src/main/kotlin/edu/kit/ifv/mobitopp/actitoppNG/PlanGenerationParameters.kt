package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DayMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultDayMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultPrecursorTourParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourSuccessorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSuccessorTourParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.PrecursorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8A
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8J
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8A
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParametersStep8J
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramPerActivity
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
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10M
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10O
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParametersStep10M
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParametersStep10O
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
    // Tour start times
    val firstTourHistogramParams: ParameterCollectionStep10M = ParametersStep10M,
    val secondTourHistogramParams: ParameterCollectionStep10O = ParametersStep10O,
    // Time budget
    val educationTimeBudgetParams: EducationBudgetSet = EducationBudget,
    val workTimeBudgedParams: WorkBudgetSet = WorkBudgets,
    val leisureTimeBudgetParams: LeisureBudgetSet = LeisureBudgets,
    val shoppingTimeBudgetParams: ShoppingBudgetSet = ShoppingBudgets,
    val transportTimeBudgetParams: TransportBudgetSet = TransportBudgets,
    val histograms: HistogramPerActivity =
        context(TimeBudgetParameters(
            educationTimeBudgetParams = educationTimeBudgetParams,
            workTimeBudgedParams = workTimeBudgedParams,
            leisureTimeBudgetParams = leisureTimeBudgetParams,
            shoppingTimeBudgetParams = shoppingTimeBudgetParams,
            transportTimeBudgetParams = transportTimeBudgetParams,
        )) {
            HistogramPerActivity.DEFAULT
        },
    // Plan generation
    val firstActivityUsesStandardDurationParams: ParameterCollectionStep8A = ParametersStep8A,
    val minorActivityDurationParams: ParameterCollectionStep8J = ParametersStep8J,
    val majorActivityDurationParams: ParameterCollectionStep8D = ParametersStep8D,
    val leadActivityDurationParams: ParameterCollectionStep8B = ParametersStep8B,
    // mobilitystructure
    val mainActivityChoiceModelParams: DayMainActivitySet = DefaultDayMainActivityParameters,
    val precursorAmountChoiceModelParams: PrecursorTourAmountSet = DefaultPrecursorTourParameters,
    val successorAmountChoiceModelParams: SuccessorTourAmountSet = DefaultSuccessorTourParameters,
    val sideActivityChoiceModelParams: SideActivitySet = DefaultSideActivityParameters,
    val tourMainActivityChoiceModelParams: SideTourMainActivitySet = DefaultSideTourMainActivityParameters,
    val step5AWithParamsParams: SideTourPrecursorSet = DefaultSideTourPrecursorParameters,
    val step5BWithParamsParams: SideTourSuccessorSet = DefaultSideTourSuccessorParameters
)

data class TimeBudgetParameters(
    val educationTimeBudgetParams: EducationBudgetSet = EducationBudget,
    val workTimeBudgedParams: WorkBudgetSet = WorkBudgets,
    val leisureTimeBudgetParams: LeisureBudgetSet = LeisureBudgets,
    val shoppingTimeBudgetParams: ShoppingBudgetSet = ShoppingBudgets,
    val transportTimeBudgetParams: TransportBudgetSet = TransportBudgets,
)