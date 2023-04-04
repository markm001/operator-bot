package com.ccat.operator.model.entity

data class VoiceStatus (
    val guildId: Long,
    val channelId: Long,
    val memberList: MutableList<Long>
)

data class VoiceChannelActivity (
    val channelId: Long,
    val memberList: MutableList<Long>
)

data class VoiceStatusDto (
    val timestamp: String,
    val channelActivities: List<VoiceChannelActivity>
): AnalyticsDataObject