package com.ccat.operator.model.entity

data class InviteUser (
    val userId:Long,
    val guildId: Long,
    val inviteCode: String
)

data class InviteStatisticsResponse (
    val inviteCode: String,
    val usesAmount: Int
)