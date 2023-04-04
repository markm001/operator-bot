package com.ccat.operator.model.entity

data class InviteUser (
    val userId:Long,
    val guildId: Long,
    val inviteCode: String
)

data class InviteStatisticsDto (
    val timestamp: String,
    val usesPerInviteCode: Map<String, Int>
): AnalyticsDataObject