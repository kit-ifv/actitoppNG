package edu.kit.ifv.mobitopp.actitopp.utils

enum class Position {
    BEFORE, MAIN, AFTER;

    companion object {
        fun fromRelativeIndex(index: Int) = when (index) {
            in Int.MIN_VALUE..<0 -> BEFORE
            0 -> MAIN
            in 1..Int.MAX_VALUE -> AFTER
            else -> throw NoSuchElementException("The branches above should be exhaustive")
        }


    }
}