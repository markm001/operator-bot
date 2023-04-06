package com.ccat.operator.utils

import io.github.oshai.KotlinLogging

object ErrorLogger {
    private val logger = KotlinLogging.logger{ }

    fun catch(error: Throwable) {
        logger.error("Encountered Error in ${error.javaClass.simpleName} - ${error.message}")
    }
}