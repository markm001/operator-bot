package com.ccat.operator.listeners

import com.ccat.operator.model.service.InviteLinkService
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class MemberJoinListener(
    private val inviteService: InviteLinkService
): ListenerAdapter() {
    /**
     * TODO: Analytics sent PER HOUR!
     */
    override fun onReady(event: ReadyEvent) {
        event.jda.guilds.forEach {guild ->
            initializeInvitesForGuild(guild)
        }
    }

    override fun onGuildInviteCreate(event: GuildInviteCreateEvent) {
        initializeInvitesForGuild(event.guild)
    }

    /**
     * Updates the all saved Invites for the specified Guild with the retrieved values
     * Used onReady & when new Invite is created
     */
    private fun initializeInvitesForGuild(guild: Guild) {
        val guildId = guild.idLong

        guild.retrieveInvites().queue { invites ->
            inviteService.initGuildInviteUses(
                guildId,
                invites.associate { it.code to it.uses }
            )
        }
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        event.guild.retrieveInvites().queue { invites ->
            val userId = event.member.idLong
            val guildId = event.guild.idLong

            val invitesMap = invites.associate { it.code to it.uses }
            val usedInviteCode:String = inviteService
                .getUsedLinkAndIncrement(guildId, invitesMap) ?: return@queue

            inviteService.incrementInviteLinkUses(guildId, usedInviteCode, userId)
        }
    }
}