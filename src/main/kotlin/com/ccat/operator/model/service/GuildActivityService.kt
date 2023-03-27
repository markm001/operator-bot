package com.ccat.operator.model.service

import com.ccat.operator.model.entity.GuildMemberActivities
import com.ccat.operator.model.entity.MemberActivityResponse
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.springframework.stereotype.Service

@Service
class GuildActivityService {
    private val activities: MutableList<GuildMemberActivities> = mutableListOf()

    /**
     * Initializes all current ActivityType.PLAYING Activities for each managed Guild
     *
     * @param guilds List of all managed Guilds at the start-up time
     */
    fun initializeGuildActivities(guilds: List<Guild>) {
        val guildMemberActivities = guilds.map { guild ->
            val memberActivities: MutableMap<Long, Activity> = getActivitiesForAllGuildMembers(guild)

            return@map GuildMemberActivities(guild.idLong, memberActivities)
        }.toMutableList()

        activities.addAll(guildMemberActivities)
    }

    /**
     * Updates the Activity for a specified UserID
     * or initializes a new Instance for the Guild if none was found
     *
     * @param guild The JDA.Guild triggering the Activity-Event
     * @param activity The JDA.Activity at the time the Event is fired.
     */
    fun updateOrCreateUserActivity(guild: Guild, userId: Long, activity: Activity) {
        val guildActivity = activities.firstOrNull { it.guildId == guild.idLong }

        if (guildActivity == null) {
            val memberActivities: MutableMap<Long, Activity> = getActivitiesForAllGuildMembers(guild)
            activities.add(GuildMemberActivities(guild.idLong, memberActivities))

            return
        }

        guildActivity.activityList[userId] = activity
    }

    /**
     * Removes the Activity for the specified UserID if it exists.
     */
    fun removeUserActivityIfExists(guildId: Long, userId: Long) {
        val userActivity = checkIfUserActivityExists(guildId, userId) ?: return
        val index = activities.indexOf(userActivity)

        userActivity.activityList.remove(userId)
        activities[index] = userActivity
    }

    private fun getActivitiesForAllGuildMembers(guild: Guild) = guild.members
        .filterNot { it.user.isBot || it.activities.isEmpty() }
        .associate { member ->
            val userId = member.user.idLong
            val activityForUser = member.activities
                .firstOrNull { it.type == Activity.ActivityType.PLAYING } ?: Activity.competing("NULL")

            return@associate userId to activityForUser
        }
        .filterValues { it.type == Activity.ActivityType.PLAYING }
        .toMutableMap()

    private fun checkIfUserActivityExists(guildId: Long, userId: Long): GuildMemberActivities? {
        return activities
            .firstOrNull { it.guildId == guildId && it.activityList.keys.contains(userId) }
    }

    /**
     * Returns the Response Object for the specified Guild
     *
     * @param guildId The ID of the Guild
     * @return MemberActivityResponse containing activity name & amount of Players
     * or an empty List if the guildID wasn't initialized
     */
    fun getGuildActivitiesForGuild(guildId: Long): List<MemberActivityResponse> {
        val guildActivities = activities.firstOrNull { it.guildId == guildId }
            ?: return emptyList()

        return guildActivities.activityList.values
            .groupingBy { it.name }
            .eachCount()
            .map { MemberActivityResponse(it.key, it.value) }
    }
}