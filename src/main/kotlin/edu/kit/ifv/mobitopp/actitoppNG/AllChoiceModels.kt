package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.tourMainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourMainActivityParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourPrecursorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSideTourSuccessorParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.DefaultSuccessorTourParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourMainActivitySet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourPrecursorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SideTourSuccessorSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.parameters.SuccessorTourAmountSet
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.ActivityAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.LEAD
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MINOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8J
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramPerActivity
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10M
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10O
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.activityAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.defaultWorkDayChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.educationDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.homeDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.leisureDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.serviceDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.shoppingDaysChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.weekroutine.choicemodels.tourAmountChoiceModel
import edu.kit.ifv.mobitopp.discretechoice.models.FixedChoiceModel

data class AllChoiceModels(
    // week routine
    val activityAmount: FixedChoiceModel<Int, PersonAlternative>,
    val educationDay: FixedChoiceModel<Int, PersonAlternative>,
    val homeDays:  FixedChoiceModel<Int, PersonAlternative>,
    val leisureDays: FixedChoiceModel<Int, PersonAlternative>,
    val serviceDays:  FixedChoiceModel<Int, PersonAlternative>,
    val shoppingDays: FixedChoiceModel<Int, PersonAlternative>,
    val tourAmount: FixedChoiceModel<Int, PersonAlternative>,
    val defaultWorkDay: FixedChoiceModel<Int, PersonAlternative>,
    // Tour start times
    val firstTourHistogram: ActivityDurationHistograms<ParameterCollectionStep10M>,
    val secondTourHistogram:  ActivityDurationHistograms<ParameterCollectionStep10O>,
    // Time budget
    val histograms: HistogramPerActivity,
    // Plan generation
    val firstActivityUsesStandardDuration: FixedChoiceModel<Boolean, BooleanDecisionAlternative>,
    val minorActivityDurationChoiceModel: ActivityDurationHistograms<ParameterCollectionStep8J>,
    val majorActivityDurationChoiceModel:  ActivityDurationHistograms<ParameterCollectionStep8D>,
    val leadActivityDurationChoiceModel:  ActivityDurationHistograms<ParameterCollectionStep8B>,
    // mobilitystructure
    val mainActivityChoiceModel:  FixedChoiceModel<ActivityType, DayAlternative>,
    val precursorAmountChoiceModel:  FixedChoiceModel<Int, PreviousDayAlternative>,
    val successorAmountChoiceModel: FixedChoiceModel<Int, PreviousDayAlternative>,
    val sideActivityChoiceModel:  FixedChoiceModel<ActivityType, ActivityAlternative>,
    val tourMainActivityChoiceModel:  FixedChoiceModel<ActivityType, TourAlternative>,
    val sideTourPrecursorActivityCountChoiceModel: FixedChoiceModel<Int, TourAlternativeInt>,
    val sideTourSuccessorActivityCountChoiceModel: FixedChoiceModel<Int, TourAlternativeInt>,
) {
    companion object {
        fun create(params: PlanGenerationParameters): AllChoiceModels {
            context(params) {
                return AllChoiceModels(
                    activityAmount = activityAmountChoiceModel,
                    educationDay = educationDaysChoiceModel,
                    homeDays = homeDaysChoiceModel,
                    leisureDays = leisureDaysChoiceModel,
                    serviceDays = serviceDaysChoiceModel,
                    shoppingDays = shoppingDaysChoiceModel,
                    tourAmount = tourAmountChoiceModel,
                    defaultWorkDay = defaultWorkDayChoiceModel,
                    firstTourHistogram = FIRST_TOUR_HISTOGRAM,
                    secondTourHistogram = SECOND_TOUR_HISTOGRAM,
                    histograms = HistogramPerActivity.DEFAULT,
                    firstActivityUsesStandardDuration = firstActivityUsesStandardDuration,
                    minorActivityDurationChoiceModel = MINOR,
                    majorActivityDurationChoiceModel = MAJOR,
                    leadActivityDurationChoiceModel = LEAD,
                    mainActivityChoiceModel = mainActivityChoiceModel,
                    precursorAmountChoiceModel = precursorAmountChoiceModel,
                    successorAmountChoiceModel = successorAmountChoiceModel,
                    sideActivityChoiceModel = sideActivityChoiceModel,
                    tourMainActivityChoiceModel = tourMainActivityChoiceModel,
                    sideTourPrecursorActivityCountChoiceModel = step5AWithParams,
                    sideTourSuccessorActivityCountChoiceModel = step5BWithParams
                )
            }
        }
    }
}