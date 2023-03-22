package com.ccat.operator.listeners

import com.ccat.operator.model.entity.MemberStatus
import com.ccat.operator.model.service.MemberStatusService
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class StatusListener(
    private val statusService: MemberStatusService
) : ListenerAdapter() {
    /**
     * Set MemberStates for all Guild upon Initialization
     */
    override fun onReady(event: ReadyEvent) {
        val guilds = event.jda.guilds
        val selfUID = event.jda.selfUser.idLong

        guilds.forEach { guild ->
            val memberStatus = checkGuildMemberStatistics(guild, selfUID)
            statusService.memberStatus[guild.idLong] = memberStatus
        }
    }

    /**
     * Set MemberStates for Guild when Online Status was Updated.
     */
    override fun onUserUpdateOnlineStatus(event: UserUpdateOnlineStatusEvent) {
        val guild = event.guild
        val selfUID = event.jda.selfUser.idLong

        statusService.memberStatus[guild.idLong] = checkGuildMemberStatistics(guild, selfUID)

        println(statusService.getStatusByGuildId(guild.idLong))
    }

    /**
     * Get the amount of Online/Idle/Dnd/Offline Members for Guild
     */
    private fun checkGuildMemberStatistics(guild:Guild, selfUID:Long): MemberStatus {
        val statusMap:Map<OnlineStatus, Int> = guild.members
            .filter { it.idLong != selfUID }
            .map { it.onlineStatus }
            .groupingBy { it }
            .eachCount()

        return MemberStatus(
            guild.members.size,
            statusMap[OnlineStatus.ONLINE] ?: 0,
            statusMap[OnlineStatus.IDLE] ?: 0,
            statusMap[OnlineStatus.DO_NOT_DISTURB] ?: 0,
            statusMap[OnlineStatus.OFFLINE] ?: 0,
        )
    }
}

