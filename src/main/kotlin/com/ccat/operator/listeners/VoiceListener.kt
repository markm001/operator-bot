package com.ccat.operator.listeners

import com.ccat.operator.model.service.VoiceChannelService
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class VoiceListener(
    private val voiceService: VoiceChannelService
) : ListenerAdapter() {
    /**
     * TODO: Analytics sent PER HOUR!
     */
    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val userId = event.member.idLong
        val guildId = event.guild.idLong
        val channelJoined = event.channelJoined
        val channelLeft = event.channelLeft

        if (channelJoined != null) {
            voiceService.addOrCreateVoiceStatus(guildId, channelJoined.asVoiceChannel(), userId)
        }

        if (channelLeft != null) {
            val channelLeftId = event.channelLeft!!.idLong
            voiceService.removeVoiceStatusMemberIfExists(guildId, channelLeftId, userId)
        }
    }
}