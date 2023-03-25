package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberStatus
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberStatusService {
    val memberStatus: MutableMap<Long, MemberStatus> = Collections.synchronizedMap(mutableMapOf())

    /**
     * Set MemberStates for Guild when Online Status was Updated.
     */
    fun onUserUpdateOnlineStatus(event: UserUpdateOnlineStatusEvent) {
        val guild = event.guild
        val selfUID = event.jda.selfUser.idLong

        memberStatus[guild.idLong] = checkGuildMemberStatistics(guild.members, selfUID)
    }

    /**
     * Get the amount of Online/Idle/Dnd/Offline Members for Guild
     */
    fun checkGuildMemberStatistics(guildMembers: List<Member>, selfUID: Long): MemberStatus {
        val statusMap:Map<OnlineStatus, Int> = guildMembers
            .filter { it.idLong != selfUID }
            .map { it.onlineStatus }
            .groupingBy { it }
            .eachCount()

        return MemberStatus(
            guildMembers.size,
            statusMap[OnlineStatus.ONLINE] ?: 0,
            statusMap[OnlineStatus.IDLE] ?: 0,
            statusMap[OnlineStatus.DO_NOT_DISTURB] ?: 0,
            statusMap[OnlineStatus.OFFLINE] ?: 0,
        )
    }

    /**
     * Get the guild specific OnlineStates
     *
     * @param guildId The ID of the Guild.
     * @return A MemberStatus Object containing user amount for each JDA.OnlineStatus
     */
    fun getStatusByGuildId(guildId: Long): MemberStatus? {
        return memberStatus[guildId]
    }
}