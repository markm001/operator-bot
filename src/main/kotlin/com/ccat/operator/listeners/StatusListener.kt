package com.ccat.operator.listeners

import com.ccat.operator.model.service.MemberStatusService
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class StatusListener(
    private val statusService: MemberStatusService
) : ListenerAdapter() {
    /**
     * TODO: Analytics sent PER HOUR!
     *
     * TODO: Change this to only update the User Online Status, not the entire Guild
     */
    override fun onUserUpdateOnlineStatus(event: UserUpdateOnlineStatusEvent) {
        if(event.user.isBot) return

        val guild = event.guild
        val selfUID = event.jda.selfUser.idLong

        val guildMembers: List<Member> = guild.members
            .filter { !(it.idLong == selfUID || it.user.isBot) }

        statusService.setMemberStatesForGuild(guildMembers, guild.idLong)
    }
}

