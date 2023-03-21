package com.ccat.operator.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand

class PingCommand : SlashCommand() {
    init {
        commandData = Commands.slash("ping", "Get a ping response!")
            .setGuildOnly(true)
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val gatewayPing = event.jda.gatewayPing
        event.reply("Pong! $gatewayPing ms").queue()
    }
}

