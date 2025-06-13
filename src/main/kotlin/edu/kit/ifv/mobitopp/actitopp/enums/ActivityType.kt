package edu.kit.ifv.mobitopp.actitopp.enums

import java.util.Collections
import java.util.EnumSet
enum class ActivityType(val typeasChar: Char, val defaultActivityTime: Int = 278) {
    EDUCATION('E', 340),
    HOME('H'),
    LEISURE('L', 130),
    SHOPPING('S', 41),
    TRANSPORT('T', 15),
    WORK('W', 472);


    companion object {
        val OUTOFHOMEACTIVITY: Set<ActivityType> = Collections.unmodifiableSet(
            EnumSet.of(
                WORK,
                EDUCATION,
                LEISURE,
                SHOPPING,
                TRANSPORT
            )
        )

        val FULLSET: Set<ActivityType> = Collections.unmodifiableSet(
            EnumSet.of(
                EDUCATION,
                HOME,
                LEISURE,
                SHOPPING,
                TRANSPORT,
                WORK,
            )
        )

    }
}
