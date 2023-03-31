package com.ccat.operator.model.service

import com.ccat.operator.client.GithubClient
import com.ccat.operator.model.entity.GuildInteraction
import com.ccat.operator.model.entity.InteractionResponse
import org.springframework.stereotype.Service
import java.net.URI

@Service
class MessageService(
    private val githubClient: GithubClient
) {
    private val interactions: MutableMap<Long, GuildInteraction> = mutableMapOf()

    private val INVITE_URL = Regex("(https?://|http?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite|discord.com/invite)/[^\\s/]+?(?=\\b)")
    private val URL_PATTERN = Regex("((?:http|https)(:/{2}\\w+)([/|.]?)([^\\s\"]*))",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))


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


    /**
     * Checks if message contains a server advertisement link, such as: discord.gg/xyz
     *
     * @param messageContent The message.rawContent String
     * @return returns true if the URL pattern is matched
     */
    fun checkMessageForAdvertisement(messageContent: String): Boolean {
        return INVITE_URL.containsMatchIn(messageContent)
    }
    /**
     * Checks if message contains a URL and if it is a malicious Link by comparing to a maintained List.
     *
     * @param messageContent The message.rawContent String
     * @return returns true if the URL is found in the List of suspicious Links
     */
    fun checkMessageForMaliciousLink(messageContent: String): Boolean {
        val cleanedString = messageContent.replace(Regex("\\p{C}"), "")

        val match = URL_PATTERN.find(cleanedString)?: return false
        val url = match.value.split(" ").first()

        try {
            val host = URI(url).host ?: throw Exception()
            return githubClient.compareSuspiciousDomains(host)
        } catch(e: Exception) {
            //TODO: Log this
            e.printStackTrace()
            throw(e)
        }
    }
}