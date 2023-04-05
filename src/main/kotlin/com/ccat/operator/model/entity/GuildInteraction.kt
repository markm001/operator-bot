package com.ccat.operator.model.entity

data class GuildInteraction (
    val channelList: MutableMap<Long, Int>,
    val userList: MutableMap<Long, Int>
)

data class MessageInteractionsDto(
    val timestamp: String,
    val totalMessages: Int,
    val uniqueChannels: Map<Long, Int>,
    val uniqueUsers: Map<Long, Int>
)