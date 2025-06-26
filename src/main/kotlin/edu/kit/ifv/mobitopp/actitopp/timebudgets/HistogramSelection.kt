package edu.kit.ifv.mobitopp.actitopp.timebudgets

import edu.kit.ifv.mobitopp.actitopp.IPerson
import kotlin.random.Random

interface HistogramSelection {

    fun select(
        random: Random,
        finalizedActivityPattern: FinalizedActivityPattern,
        person: IPerson,
    ): ArrayHistogram
}