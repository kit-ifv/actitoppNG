package edu.kit.ifv.mobitopp.actitopp.steps.step7

import edu.kit.ifv.mobitopp.actitopp.IPerson

interface HistogramSelection {

    fun select(randomNumber: Double, finalizedActivityPattern: FinalizedActivityPattern, person: IPerson): ArrayHistogram
}