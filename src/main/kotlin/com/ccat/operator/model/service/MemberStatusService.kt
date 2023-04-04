package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberStates
import com.ccat.operator.model.entity.MemberStatesDto
import com.ccat.operator.utils.TimestampUtils
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import org.springframework.stereotype.Service

@Service
class MemberStatusService {
    private val guildStates: MutableList<MemberStates> = mutableListOf()

    /**
     * Initializes or updates the MemberStates Object for a particular GuildID
     *
     * @param members The List of JDA.Guild Members
     * @param guildId The Guild ID
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
     * @return A MemberStatesDto Object containing user amount for each JDA.OnlineStatus & Timestamp
     */
    fun getMemberStatesByGuildId(guildId: Long): MemberStatesDto? {
        val states = guildStates.firstOrNull { it.guildId == guildId }?: return null

        return MemberStatesDto(
            TimestampUtils.getCurrentTime(),
            states.total,
            states.online,
            states.idle,
            states.dnd
        )
    }
}