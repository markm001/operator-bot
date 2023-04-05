package com.ccat.operator.utils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Used to retrieve the String representation of the current Timestamp (truncated to Hours).
 * ! Important: The setter has to be invoked in order to the proper time to be set.
 *
 * @sample setCurrentTime
 * @sample getCurrentTime
 */
object TimestampUtils {
    private var currentTimeString: String = ""

    /**
     * Sets the Timestamp to the current time
     */
    fun setCurrentTime() {
        currentTimeString = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).toString()
    }

    fun getCurrentTime():String {
        return currentTimeString
    }
}