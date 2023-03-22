package com.ccat.operator.commands

import com.ccat.operator.utils.ResponseHandler
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand

class SayCommand : SlashCommand() {
    init {
        commandData = Commands.slash("say", "Make the Bot repeat your input")
            .addOption(
                OptionType.STRING,
                "input", "The string to be repeated",
                true)
            .setGuildOnly(true)
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val optionMapping = event.getOption("input")

        if(optionMapping == null) {
            ResponseHandler.missingArgs(event).queue()
            return
        }

        event.channel.sendMessage(optionMapping.asString)
            .setAllowedMentions(setOf(Message.MentionType.EMOJI, Message.MentionType.CHANNEL))
            .queue()

        event.reply("Message sent").setEphemeral(true).queue()
    }
}