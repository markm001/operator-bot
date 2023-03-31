package com.ccat.operator.listeners

import com.ccat.operator.model.service.MessageService
import com.ccat.operator.model.service.ModerationService
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Check if Message Link contains invite or spam
 */
class MessageModerationListener(
    private val moderationService: ModerationService,
    private val messageService: MessageService
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild || event.author.isBot || event.author.isSystem) return
        val member = event.member ?: return
        val self = event.guild.selfMember

        checkMessageContent(event.message, member, self)
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        if (!event.isFromGuild || event.author.isBot || event.author.isSystem) return
        val member = event.member ?: return
        val self = event.guild.selfMember

        checkMessageContent(event.message, member, self)
    }

    /**
     * Check if Message Content is forbidden
     *
     * @param message The Guild Message sent by the member
     * @param member The Guild Member sending the message
     * @param self The Bot itself
     */
    private fun checkMessageContent(message: Message, member: Member, self: Member) {
        val messageContent = message.contentRaw

        if (messageService.checkMessageForAdvertisement(messageContent))
            purgeMessage(
                message, member, self,
                Duration.of(15, ChronoUnit.MINUTES), "Advertising other discord servers."
            )

        if (!(messageContent.contains("http://")
                    || messageContent.contains("https://"))) return

        if (messageService.checkMessageForMaliciousLink(messageContent))
            purgeMessage(
                message, member, self,
                Duration.of(20, ChronoUnit.DAYS), "Posting forbidden links."
            )
    }

    /**
     * Purges the specific message and enforces a timeout on the member
     */
    private fun purgeMessage(message: Message, member: Member, selfMember: Member, duration: Duration, reason: String) {
        message.delete().queue()
        moderationService.timeoutGuildMember(
            member,
            duration,
            reason,
            selfMember
        )
    }
}