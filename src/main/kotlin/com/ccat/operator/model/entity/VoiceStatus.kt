package com.ccat.operator.model.entity

data class VoiceStatus (
    val guildId: Long,
    val channelId: Long,
    val memberList: MutableList<Long>
)