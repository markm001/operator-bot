package com.ccat.operator.model.entity

data class MemberStatesDto (
    val timestamp: String,
    val total: Int,
    val online: Int,
    val idle: Int,
    val doNotDisturb: Int
): AnalyticsDataObject