package com.ccat.operator.model.service

import com.ccat.operator.model.entity.VoiceStatus
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import org.springframework.stereotype.Service

@Service
class VoiceChannelService {
    private val voiceChannels: MutableList<VoiceStatus> = mutableListOf()

    fun removeVoiceStatusMemberIfExists(guildId: Long, channelId: Long, userId: Long) {
        val voiceStatus = checkIfVoiceStatusExists(guildId, channelId)
            ?: return

        voiceStatus.memberList.remove(userId)
    }

    fun addOrCreateVoiceStatus(
        guildId: Long,
        channelJoinedId: Long,
        channelJoined: VoiceChannel,
        userId: Long
    ) {
        val voiceStatus = checkIfVoiceStatusExists(guildId, channelJoinedId)

        if (voiceStatus == null) {
            voiceChannels.add(
                createVoiceStatusForChannel(channelJoined)
            )
        } else {
            voiceStatus.memberList.add(userId)
        }
    }

    fun initializeVoiceChannelActivity(guilds: List<Guild>) {
        val voiceChannelStatus = guilds.flatMap { it.voiceChannelCache }
            .filter { it.members.isNotEmpty() }
            .map {
                return@map createVoiceStatusForChannel(it)
            }.toMutableList()

        voiceChannels.addAll(voiceChannelStatus)
    }


    private fun checkIfVoiceStatusExists(guildId: Long, channelId: Long): VoiceStatus? {
        return voiceChannels.firstOrNull {
            it.guildId == guildId
                    && it.channelId == channelId
        }
    }

    private fun createVoiceStatusForChannel(channel: VoiceChannel): VoiceStatus {
        val guildId = channel.guild.idLong
        val channelId = channel.idLong
        val memberList: List<Long> = channel.members.map { member -> member.idLong }

        return VoiceStatus(guildId, channelId, memberList.toMutableList())
    }

    fun getVoiceChannelStatusForGuild(guildId: Long): List<VoiceStatus> {
        return voiceChannels.filter { it.guildId == guildId }
    }
}