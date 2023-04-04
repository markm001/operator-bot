package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberActivityDto
import com.ccat.operator.utils.TimestampUtils
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.springframework.stereotype.Service

@Service
class GuildActivityService {
    fun getActivitiesForAllGuildMembers(guild: Guild) = guild.members
        .filterNot { it.user.isBot || it.activities.isEmpty() }
        .associate { member ->
            val userId = member.user.idLong
            val activityForUser = member.activities
                .firstOrNull { it.type == Activity.ActivityType.PLAYING } ?: Activity.competing("NULL")

            return@associate userId to activityForUser
        }
        .filterValues { it.type == Activity.ActivityType.PLAYING }
        .toMutableMap()

    fun mapToMemberActivity(memberActivities: Map<Long, Activity>):MemberActivityDto? {
        val activities: Map<String, Int> = memberActivities.values
            .groupingBy { it.name }
            .eachCount()

        if(activities.isEmpty()) return null

        return MemberActivityDto(
            TimestampUtils.getCurrentTime(),
            activities
        )
    }
}