package com.ccat.operator.commands

import com.ccat.operator.model.service.ModerationService
import com.ccat.operator.utils.ResponseHandler
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand
import java.time.Duration
import java.time.temporal.ChronoUnit

class TimeoutCommand(
    private val moderationService: ModerationService
) : SlashCommand() {
    init {
        commandData = Commands.slash("timeout", "Timeout a User")
            .setGuildOnly(true)
            .addOptions(
                OptionData(OptionType.USER, "member", "The effected Guild Member", true),
                OptionData(OptionType.STRING, "reason", "Reason for enforcing the timeout", true),
                OptionData(OptionType.INTEGER, "duration", "The duration of the timeout", true),
                OptionData(OptionType.STRING, "time_unit", "The time unit of the duration", true)
                    .addChoice("second", "SECONDS")
                    .addChoice("minutes", "MINUTES")
                    .addChoice("hours", "HOURS")
                    .addChoice("days", "DAYS")
            )
    }

    override fun execute(event: SlashCommandInteractionEvent) {
        val member = event.member?: return
        if(!member.hasPermission(Permission.KICK_MEMBERS))
            return event.reply("Missing perms").queue() //Todo: Missing permission in Handler

        val memberOption = event.getOption("member")
        val reasonOption = event.getOption("reason")
        val durationOption = event.getOption("duration")
        val unitOption = event.getOption("time_unit")

        if (memberOption == null || reasonOption == null
            || durationOption == null || unitOption == null || event.member == null) {
            return ResponseHandler.missingArgs(event).queue()
        }

        //TODO: replace with Member not found Response
        val effectedMember = memberOption.asMember ?: return ResponseHandler.missingArgs(event).queue()

        val timeoutDuration = Duration.of(
            durationOption.asLong,
            ChronoUnit.valueOf(unitOption.asString)
        )

        moderationService.timeoutGuildMember(effectedMember,
            timeoutDuration,
            reasonOption.asString,
            event.member!!)
        event.reply("The user $effectedMember has been timed out.").setEphemeral(true).queue()
    }
}