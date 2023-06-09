package com.ccat.operator.listeners

import com.ccat.operator.model.service.MessageService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener(
    private val messageService: MessageService
): ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if(!event.isFromGuild || event.author.isBot || event.author.isSystem) return

        val guildId = event.guild.idLong
        val channelId: Long = event.channel.asTextChannel().idLong
        val senderId: Long = event.author.idLong

        messageService.incrementMessagesForGuild(guildId, channelId, senderId)
    }
}