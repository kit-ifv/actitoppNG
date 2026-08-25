package edu.kit.ifv.mobitopp.actitoppNG.timebudgets

import edu.kit.ifv.mobitopp.actitoppNG.enums.Category
import edu.kit.ifv.mobitopp.actitoppNG.utils.ceilWholeMinutes
import edu.kit.ifv.mobitopp.actitoppNG.utils.indexBinarySearch
import it.unimi.dsi.fastutil.doubles.DoubleArrayList
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes



class ChangeTrackingArrayHistogram(private val original: Histogram) : ModifiableHistogram, Histogram by original{
    private val previouslySelectedIndices: MutableList<Int> = mutableListOf()
    context(rng: Random)
    override fun select(lowerBoundInclusive: Duration?, upperBoundInclusive: Duration?): Duration {
        val index = potentiallySelect()
        if (index != NO_PRESELECT && index < previouslySelectedIndices.size) {
            val element = previouslySelectedIndices[index]
            val lb = lowerBoundInclusive?.ceilWholeMinutes
            val ub = upperBoundInclusive?.inWholeMinutes?.toInt()
            if (
                (lb == null || element >= lb) &&
                (ub == null || element <= ub)
            ) {
                return (element + offset).minutes
            }
        }
        return original.select(lowerBoundInclusive, upperBoundInclusive)
    }
//    override fun selectInt(randomNumber: Double, lowerBoundInclusive: Int?, upperBoundInclusive: Int?): Duration {
//        val index = potentiallySelect(randomNumber)
//        if (index != NO_PRESELECT && index < previouslySelectedIndices.size) {
//            val element = previouslySelectedIndices[index]
//
//            if (
//                (lowerBoundInclusive == null || element >= lowerBoundInclusive) &&
//                (upperBoundInclusive == null || element <= upperBoundInclusive)
//            ) {
//                return (element + offset).minutes
//            }
//        }
//
//        return super.selectInt(randomNumber, lowerBoundInclusive, upperBoundInclusive)
//    }

    override fun modify(position: Int) {
        previouslySelectedIndices.add(position - offset)
    }

    override fun cleanseByLoading(original: ArrayHistogram) {
        previouslySelectedIndices.clear()
    }

    context(rng: Random)
    private fun potentiallySelect(): Int {
        val randomNumber = rng.nextDouble()
        val targetK: Int = previouslySelectedIndices.size
        if (getForSize(targetK) >= randomNumber) {
            return getActually(randomNumber, targetK)
        }
        return NO_PRESELECT

    }

    companion object {
        private const val NO_PRESELECT: Int = -1

        private val cumsum = DoubleArrayList()


        fun getForSize(targetK: Int): Double {

            while (cumsum.size <= targetK) {
                val previous = cumsum.lastOrNull() ?: 0.0
                cumsum.add(previous + (1.0 - previous) / 3.0)
            }
            return cumsum.getDouble(targetK)
        }

        fun getActually(randomNumber: Double, upperLimit: Int): Int {
            return cumsum.elements().indexBinarySearch(randomNumber, toIndex = upperLimit)
        }
    }
}