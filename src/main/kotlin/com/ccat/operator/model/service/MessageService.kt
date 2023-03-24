package com.ccat.operator.model.service

import com.ccat.operator.model.entity.GuildInteraction
import com.ccat.operator.model.entity.InteractionResponse
import org.springframework.stereotype.Service

@Service
class MessageService {
    private val interactions: MutableMap<Long, GuildInteraction> = mutableMapOf()

    /**
     * Increment MessageInteractions for specific Guild
     * will create or increment values for ChannelID/UserID keys
     */
    fun incrementMessagesForGuild(guildId: Long, channelId:Long, userId:Long) {
        val guildInteraction: GuildInteraction = interactions.getOrPut(guildId) {
            GuildInteraction(mutableMapOf(), mutableMapOf())
        }

        interactions[guildId] = guildInteraction.apply {
            channelList[channelId] = channelList.getOrPut(channelId){ 0 }.inc()
            userList[userId] = userList.getOrPut(userId){ 0 }.inc()
        }
    }

    /**
     * Get the guild specific InteractionResponse Object
     *
     * @param guildId The ID of the Guild.
     * @return A MemberStatus Object containing totalMessages, channels and users with amount of interactions or NULL
     */
    private fun getMessageInteractionsByGuildId(guildId: Long): InteractionResponse? {
        val guildInteractions = interactions[guildId] ?: return null

        val totalMessages = guildInteractions.userList.values.sum()

        return InteractionResponse(
            totalMessages,
            guildInteractions.channelList,
            guildInteractions.userList
        )
    }
}