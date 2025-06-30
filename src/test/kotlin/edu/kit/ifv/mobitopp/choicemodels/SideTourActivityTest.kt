package edu.kit.ifv.mobitopp.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.choicemodels.tourMainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.shenanigans.TourAlternative
import edu.kit.ifv.mobitopp.actitoppNG.randomDayStructure
import edu.kit.ifv.mobitopp.actitoppNG.randomPersonWithRoutine
import edu.kit.ifv.mobitopp.actitoppNG.steps.TourPositionAttributes
import kotlinx.serialization.KSerializer
import kotlin.random.Random

class SideTourActivityTest : ChoiceModelTest<ActivityType, TourAlternative>(tourMainActivityChoiceModel) {
    override val name: String = "tourMainActivityChoiceModel"
    override val serializer: KSerializer<ActivityType> = ActivityType.serializer()

    override fun converter(option: ActivityType): TourAlternative {
        val randomDayStructure = randomDayStructure(inputRandom)
        val (person, routine) = randomPersonWithRoutine(inputRandom)
        return TourAlternative(option, person, routine, randomDayStructure, tourAttributes = RandomTourPosAttr(inputRandom))
    }
}

class RandomTourPosAttr(random: Random): TourPositionAttributes {
    val rnd1 = random.nextBoolean()
    val rnd2 = random.nextBoolean()
    val rnd3 = random.nextBoolean()
    val rnd4 = random.nextBoolean()
    val rnd5 = random.nextBoolean()

    override fun isFirstTourOfDay(): Boolean = rnd1

    override fun isSecondTourOfDay(): Boolean = rnd2

    override fun isThirdTourOfDay(): Boolean = rnd3

    override fun isBeforeMainTour(): Boolean = rnd4

    override fun isAfterMainTour(): Boolean = rnd5
}