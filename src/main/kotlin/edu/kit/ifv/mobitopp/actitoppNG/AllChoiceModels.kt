package edu.kit.ifv.mobitopp.actitoppNG

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.mainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.precursorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5AWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.step5BWithParams
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.successorAmountChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.tourMainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.ActivityAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.DayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.PreviousDayAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternative
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternativeInt
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.BooleanDecisionAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.LEAD
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MAJOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MINOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.firstActivityUsesStandardDuration
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8B
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8D
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8J
import edu.kit.ifv.mobitopp.actitoppNG.steps.PersonAlternative
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.HistogramPerActivity
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.FIRST_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.choicemodels.SECOND_TOUR_HISTOGRAM
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10M
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.parameters.ParameterCollectionStep10O
import edu.kit.ifv.mobitopp.actitoppNG.tourstarttimes.standardPreferredTourStartChoiceModel
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
    val activityAmountChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    val educationDaysChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    val homeDaysChoiceModel:  FixedChoiceModel<Int, PersonAlternative>,
    val leisureDaysChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    val serviceDaysChoiceModel:  FixedChoiceModel<Int, PersonAlternative>,
    val shoppingDaysChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    val tourAmountChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    val defaultWorkDayChoiceModel: FixedChoiceModel<Int, PersonAlternative>,
    // Tour start times
    val firstTourHistogram: ActivityDurationHistograms<ParameterCollectionStep10M>,
    val secondTourHistogram:  ActivityDurationHistograms<ParameterCollectionStep10O>,
    val preferredTourStartChoiceModel:  FixedChoiceModel<ArrayHistogram, MainDurationAlternative>,
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
                    activityAmountChoiceModel = activityAmountChoiceModel,
                    educationDaysChoiceModel = educationDaysChoiceModel,
                    homeDaysChoiceModel = homeDaysChoiceModel,
                    leisureDaysChoiceModel = leisureDaysChoiceModel,
                    serviceDaysChoiceModel = serviceDaysChoiceModel,
                    shoppingDaysChoiceModel = shoppingDaysChoiceModel,
                    tourAmountChoiceModel = tourAmountChoiceModel,
                    defaultWorkDayChoiceModel = defaultWorkDayChoiceModel,
                    firstTourHistogram = FIRST_TOUR_HISTOGRAM,
                    secondTourHistogram = SECOND_TOUR_HISTOGRAM,
                    preferredTourStartChoiceModel = standardPreferredTourStartChoiceModel,
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
                    sideTourSuccessorActivityCountChoiceModel = step5BWithParams,
                )
            }
        }
    }
}