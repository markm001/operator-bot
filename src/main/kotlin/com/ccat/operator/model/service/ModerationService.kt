package com.ccat.operator.model.service

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import org.springframework.stereotype.Service
import java.awt.Color
import java.time.Duration
import java.time.Instant

@Service
class ModerationService {
    /**
     * Timeout a guild member for a certain duration
     *
     * @param member The Guild member to timeout
     * @param duration The Duration of the timeout
     * @param reason The reason for the timeout
     * @param moderator The moderator who enforced the timeout
     */
    fun timeoutGuildMember(member: Member, duration: Duration, reason:String, moderator: Member) {
        val guild = member.guild
        val timeoutEmbed = EmbedBuilder()
            .setColor(Color.red)
            .setAuthor(moderator.user.asTag, null, moderator.effectiveAvatarUrl)
            .setTitle("You have been timed out from ${guild.name}")
            .setDescription("Please contact the staff if wish to appeal the timeout.")
            .addField("Reason", reason, true)
            .setTimestamp(Instant.now())
            .setFooter(member.effectiveName, member.effectiveAvatarUrl)
            .build()

        guild.timeoutFor(member, duration).queue {
            member.user.openPrivateChannel().queue{
                it.sendMessageEmbeds(timeoutEmbed).queue()
            }
        }
    }
}