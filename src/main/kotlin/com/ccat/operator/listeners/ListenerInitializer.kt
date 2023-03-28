package com.ccat.operator.listeners

import com.ccat.operator.model.service.GuildActivityService
import com.ccat.operator.model.service.InviteLinkService
import com.ccat.operator.model.service.MemberStatusService
import com.ccat.operator.model.service.VoiceChannelService
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * Initializes Analytics Lists for each managed JDA.Guild on start-up
 */
class ListenerInitializer(
    private val inviteService: InviteLinkService,
    private val statusService: MemberStatusService,
    private val activityService: GuildActivityService,
    private val voiceService: VoiceChannelService
): ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        val guilds: List<Guild> = event.jda.guilds
        val selfUID = event.jda.selfUser.idLong

        guilds.forEach {guild ->
            inviteService.initializeInvitesForGuild(guild)
            activityService.initializeActivitiesForGuild(guild)
            voiceService.initializeVoiceChannelActivityForGuild(guild)

            val guildMembers: List<Member> = guild.members
                .filter { !(it.idLong == selfUID || it.user.isBot) }
            statusService.setMemberStatesForGuild(guildMembers, guild.idLong)
        }
    }
}