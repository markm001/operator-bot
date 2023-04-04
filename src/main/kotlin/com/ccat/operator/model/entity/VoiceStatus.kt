package com.ccat.operator.model.entity

data class VoiceChannelActivity (
    val channelId: Long,
    val memberList: List<Long>
)

data class VoiceStatusDto (
    val timestamp: String,
    val channelActivities: List<VoiceChannelActivity>
): AnalyticsDataObject