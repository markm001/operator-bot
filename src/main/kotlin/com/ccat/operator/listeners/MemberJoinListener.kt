package com.ccat.operator.listeners

import com.ccat.operator.model.service.InviteLinkService
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MemberJoinListener(
    private val inviteService: InviteLinkService
): ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        event.jda.guilds.forEach {
            inviteService.initializeInvitesForGuild(it)
        }
    }

    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        inviteService.initializeInvitesForGuild(event.guild)
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        event.guild.retrieveInvites().queue { invites ->
            val userId = event.member.idLong
            val guildId = event.guild.idLong

            val invitesMap = invites.associate { it.code to it.uses }
            val inviteCodeUsed: String = inviteService
                .getUsedLinkAndIncrement(guildId, invitesMap) ?: return@queue

            inviteService.saveInviteUser(guildId, userId, inviteCodeUsed)
        }
    }
}