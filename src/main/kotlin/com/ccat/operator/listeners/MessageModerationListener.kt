package com.ccat.operator.listeners

import com.ccat.operator.client.GithubClient
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.net.URI

/**
 * Check if Message Link contains invite or spam
 */
class MessageModerationListener(
    private val githubClient: GithubClient
): ListenerAdapter() {
    private val INVITE_URL = Regex("(https?://|http?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite|discord.com/invite)/[^\\s/]+?(?=\\b)")

    private val URL_PATTERN = Regex("((?:http|https)(:/{2}\\w+)([/|.]?)([^\\s\"]*))",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))


    override fun onMessageReceived(event: MessageReceivedEvent) {
        if(!event.isFromGuild || event.author.isBot || event.author.isSystem) return

        if(checkMessageContent(event.message)) event.message.delete().queue()
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        if(!event.isFromGuild || event.author.isBot || event.author.isSystem) return

        if(checkMessageContent(event.message)) event.message.delete().queue()
    }


    fun checkMessageContent(message: Message): Boolean {
        val contentRaw = message.contentRaw

        if(INVITE_URL.containsMatchIn(contentRaw)) return true

        if (!(contentRaw.contains("http://")
                    || contentRaw.contains("https://"))) return false
        val cleanedString = contentRaw.replace(Regex("\\p{C}"), "")

        val match = URL_PATTERN.find(cleanedString)?: return false
        val url = match.value.split(" ").first()

        try {
            val host = URI(url).host ?: throw Exception()
            return githubClient.compareSuspiciousDomains(host)
        } catch(e: Exception) {
            e.printStackTrace()
            throw(e)
        }
    }
}