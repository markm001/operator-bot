package com.ccat.operator.model.service

import com.ccat.operator.model.entity.VoiceChannelActivity
import com.ccat.operator.model.entity.VoiceStatusDto
import com.ccat.operator.utils.TimestampUtils
import net.dv8tion.jda.api.entities.Guild
import org.springframework.stereotype.Service

@Service
class VoiceChannelService {
    fun getVoiceChannelActivity(guild:Guild): List<VoiceChannelActivity> {
        return guild.voiceChannelCache
            .filter { it.members.isNotEmpty() }
            .map { VoiceChannelActivity(
                it.idLong,
                it.members.map { member -> member.idLong }
            ) }
    }

    fun mapToVoiceChannelStatus(channels: List<VoiceChannelActivity>): VoiceStatusDto? {
        val channelActivities = channels.map {
            VoiceChannelActivity(
                it.channelId,
                it.memberList
            )
        }

        if(channelActivities.isEmpty()) return null

        return VoiceStatusDto(
            TimestampUtils.getCurrentTime(),
            channelActivities
        )
    }
}