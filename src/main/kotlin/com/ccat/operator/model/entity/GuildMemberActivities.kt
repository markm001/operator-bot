package com.ccat.operator.model.entity

data class MemberActivityDto (
    val timestamp: String,
    val usersPerActivity: Map<String, Int>
): AnalyticsDataObject