package com.ccat.operator.model.entity

data class VoiceStatus (
    val guildId: Long,
    val channelId: Long,
    val memberList: MutableList<Long>
)

data class VoiceStatusResponse (
    val guildId: Long,
    val channelId: Long,
    val memberList: MutableList<Long>
)