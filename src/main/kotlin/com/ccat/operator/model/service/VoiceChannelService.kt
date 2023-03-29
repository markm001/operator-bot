package com.ccat.operator.model.service

import com.ccat.operator.model.entity.VoiceStatus
import com.ccat.operator.model.entity.VoiceStatusResponse
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import org.springframework.stereotype.Service

@Service
class VoiceChannelService {
    private val voiceChannels: MutableList<VoiceStatus> = mutableListOf()

    /**
     * Create VoiceStatus Objects for specified Guild on start-up
     *
     * @param guild A Guild managed by the Bot
     */
    fun initializeVoiceChannelActivityForGuild(guild: Guild) {
        guild.voiceChannelCache
            .filter { it.members.isNotEmpty() }
            .map { createVoiceStatusForChannel(it) }
            .forEach { voiceChannels.add(it) }
    }

    /**
     * Removes the specified userID from the VoiceStatus Object List of participants if it exists
     *
     */
    fun removeVoiceStatusMemberIfExists(guildId: Long, channelId: Long, userId: Long) {
        val voiceStatus = checkIfVoiceStatusExists(guildId, channelId)
            ?: return

        voiceStatus.memberList.remove(userId)
    }

    /**
     * Creates a VoiceStatus Object or modifies the existing one
     *
     * @param channel The VoiceChannel joined by the User
     */
    fun addOrCreateVoiceStatus(guildId: Long, channel: VoiceChannel, userId: Long) {
        val voiceStatus = checkIfVoiceStatusExists(guildId, channel.idLong)

        if (voiceStatus == null) {
            voiceChannels.add(
                createVoiceStatusForChannel(channel)
            )
        } else {
            voiceStatus.memberList.add(userId)
        }
    }

    /**
     * Check if a VoiceStatus Object has been initialized in the List
     */
    private fun checkIfVoiceStatusExists(guildId: Long, channelId: Long): VoiceStatus? {
        return voiceChannels.firstOrNull {
            it.guildId == guildId
            && it.channelId == channelId
        }
    }

    /**
     * Creates VoiceStatus Object for specific VoiceChannel
     * Retrieves currently joined MemberIDs from Channel
     *
     * @param channel The corresponding VoiceChannel of the Guild
     */
    private fun createVoiceStatusForChannel(channel: VoiceChannel): VoiceStatus {
        val guildId = channel.guild.idLong
        val channelId = channel.idLong
        val memberList: List<Long> = channel.members.map { member -> member.idLong }

        return VoiceStatus(guildId, channelId, memberList.toMutableList())
    }

    /**
     * Returns a List of VoiceStatus Objects for specified GuildID
     *
     * @param guildId The ID of the relevant Guild
     */
    fun getVoiceChannelStatusForGuild(guildId: Long): List<VoiceStatusResponse> {
        return voiceChannels
            .filter { it.guildId == guildId }
            .map {
                VoiceStatusResponse(
                    guildId,
                    it.channelId,
                    it.memberList
                )
            }
    }
}