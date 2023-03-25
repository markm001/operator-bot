package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberStates
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import org.springframework.stereotype.Service

@Service
class MemberStatusService {
    private val guildStates: MutableList<MemberStates> = mutableListOf()

    /**
     * Initializes or updates the MemberStates Object for a particular GuildID
     */
    fun setMemberStatesForGuild(members: List<Member>, guildId: Long) {
        val statusMap = members
            .map { it.onlineStatus }
            .groupingBy { it }
            .eachCount()

        val memberStates = MemberStates(
            guildId,
            members.size,
            statusMap[OnlineStatus.ONLINE] ?: 0,
            statusMap[OnlineStatus.IDLE] ?: 0,
            statusMap[OnlineStatus.DO_NOT_DISTURB] ?: 0,
            statusMap[OnlineStatus.OFFLINE] ?: 0,
        )

        val savedState = guildStates.firstOrNull { it.guildId == guildId }
        val index = guildStates.indexOf(savedState)
        if(savedState == null) guildStates.add(memberStates) else guildStates[index] = memberStates

    }

    /**
     * Get the guild specific OnlineStates
     *
     * @param guildId The ID of the Guild.
     * @return A MemberStates Object containing user amount for each JDA.OnlineStatus
     */
    fun getMemberStatesByGuildId(guildId: Long): MemberStates? {
        return guildStates.firstOrNull { it.guildId == guildId }
    }
}