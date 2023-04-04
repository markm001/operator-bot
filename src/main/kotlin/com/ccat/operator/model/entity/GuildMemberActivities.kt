package com.ccat.operator.model.entity

import net.dv8tion.jda.api.entities.Activity

data class GuildMemberActivities (
    val guildId: Long,
    val activityList: MutableMap<Long, Activity>
)

data class MemberActivityDto (
    val timestamp: String,
    val usersPerActivity: Map<String, Int>
): AnalyticsDataObject

interface AnalyticsDataObject { }
