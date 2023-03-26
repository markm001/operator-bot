package com.ccat.operator.listeners

import com.ccat.operator.model.service.VoiceChannelService
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class VoiceListener(
    private val voiceService: VoiceChannelService
) : ListenerAdapter() {
    /**
     * TODO: Analytics sent PER HOUR!
     */
    override fun onReady(event: ReadyEvent) {
        voiceService.initializeVoiceChannelActivity(event.jda.guilds)
    }

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val userId = event.member.idLong
        val guildId = event.guild.idLong
        val channelJoined = event.channelJoined
        val channelLeft = event.channelLeft

        if (channelJoined != null) {
            val channelJoinedId = event.channelJoined!!.idLong
            voiceService.addOrCreateVoiceStatus(guildId, channelJoinedId, channelJoined.asVoiceChannel(), userId)
        }

        if (channelLeft != null) {
            val channelLeftId = event.channelLeft!!.idLong
            voiceService.removeVoiceStatusMemberIfExists(guildId, channelLeftId, userId)
        }

        //TODO: DEBUG REMOVE LATER!
        println(voiceService.getVoiceChannelStatusForGuild(guildId))
    }
}