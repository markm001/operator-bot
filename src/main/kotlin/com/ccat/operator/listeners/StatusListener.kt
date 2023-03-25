package com.ccat.operator.listeners

import com.ccat.operator.model.service.MemberStatusService
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class StatusListener(
    private val statusService: MemberStatusService
) : ListenerAdapter() {
    /**
     * Set MemberStates for all Guild upon Initialization
     * TODO: Analytics sent PER HOUR!
     */
    override fun onReady(event: ReadyEvent) {
        val guilds = event.jda.guilds
        val selfUID = event.jda.selfUser.idLong

        guilds.forEach { guild ->
            val memberStatus = statusService.checkGuildMemberStatistics(guild.members, selfUID)
            statusService.memberStatus[guild.idLong] = memberStatus
        }
    }


}

