package com.ccat.operator.utils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TimestampUtils {
    private var currentTimeString: String = ""

    /**
     * Sets the Timestamp to the current time
     */
    fun setCurrentTime() {
        currentTimeString = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).toString()
    }

    fun getCurrentTime():String {
        return currentTimeString
    }
}