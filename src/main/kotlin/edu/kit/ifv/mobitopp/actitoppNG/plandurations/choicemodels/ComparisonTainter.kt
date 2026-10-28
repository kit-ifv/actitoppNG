package edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels

import edu.kit.ifv.mobitopp.actitoppNG.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.TaintedActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.TaintedHistogramSelector
import edu.kit.ifv.mobitopp.actitoppNG.timebudgets.ArrayHistogram
import kotlin.random.Random
import kotlin.time.Duration

class ComparisonTainter(
    private val original: TaintedHistogramSelector,
    private val comparison: TaintedActivityDurationHistograms,
): TaintedHistogramSelector {
    context(rng: Random)
    override fun chooseHistogramFromNeighbors(
        duration: Duration,
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram {
        return original.chooseHistogramFromNeighbors(duration, bounds, converter)
    }

    context(rng: Random)
    override fun select(converter: MainDurationAlternative): Duration {
        return compare(converter) {
            original.select(converter)
        }

    }

    context(rng: Random)
    override fun select(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): Duration {
        return compare(converter) {
            original.select(bounds,converter)
        }
    }


    private fun <T> compare(converter: MainDurationAlternative, lambda: () -> T): T {
        context(converter) {
            val originalProbs = original.probabilitiesSingular()
            val comparisonProbs = comparison.probabilitiesSingular()

            val bad = originalProbs.keys.filter {
                originalProbs[it]!! != comparisonProbs[it]!!
            }.associate { it.categoryIndex to (originalProbs[it]!! to comparisonProbs[it]!!)}
            if(bad.isNotEmpty()) {
                println("HAmmertime")
                val e = original.probabilitiesSingular()
                val f = comparison.probabilitiesSingular()
            }

        }
        return lambda()
    }
    context(rng: Random)
    override fun selectHistogram(
        bounds: ClosedRange<Duration>,
        converter: MainDurationAlternative,
    ): ArrayHistogram? {
        return compare(converter) {
            original.selectHistogram(bounds, converter)
        }
    }

    context(rng: Random)
    override fun selectSingular(converter: MainDurationAlternative): ArrayHistogram {
        return compare(converter) {
            original.selectSingular( converter)
        }
    }

    override fun reset() {
        original.reset()
        comparison.reset()
    }

    context(rng: Random)
    override fun selectAndTaint(
        bounds: ClosedRange<Duration>,
        duration: Duration,
        converter: MainDurationAlternative,
    ): Duration {
        return compare(converter) {
            original.selectAndTaint(bounds, duration, converter).also { dur ->
                val position = dur.inWholeMinutes.toInt()
                val thisOne = comparison.taintedHistograms.values.first {
                    position in it
                }
                thisOne.modify(position)
            }
        }
    }

    context(converter: MainDurationAlternative)
    override fun probabilitiesSingular(): Map<ArrayHistogram, Double> {
        return compare(converter) {
            original.probabilitiesSingular()
        }
    }

    override fun taint(): TaintedHistogramSelector {
        throw IllegalStateException("Shouldnt be here")
        return original.taint()
    }

}