package com.ccat.operator.model.entity

import net.dv8tion.jda.api.entities.Activity

data class GuildMemberActivities (
    val guildId: Long,
    val activityList: MutableMap<Long, Activity>
)

data class MemberActivityResponse (
    val name: String,
    val totalMembers: Int
)