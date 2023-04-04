package com.ccat.operator.schedulerjobs

import com.ccat.operator.client.FirebaseClient
import com.ccat.operator.model.entity.AnalyticsDataObject
import com.ccat.operator.model.service.*
import com.ccat.operator.utils.TimestampUtils
import net.dv8tion.jda.api.JDA
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class SendAnalyticsJob(
    private val jda: JDA,
    private val guildActivityService: GuildActivityService,
    private val inviteLinkService: InviteLinkService,
    private val memberStatusService: MemberStatusService,
    private val messageService: MessageService,
    private val voiceChannelService: VoiceChannelService,

    private val firebaseClient: FirebaseClient
) {
    //    @Scheduled(cron = "0 0 * ? * *")
    @Scheduled(cron = "0 * * ? * *") //min
    fun sendHourlyAnalytics() {
        println("Hourly Analytics Report sending...")
        TimestampUtils.setCurrentTime()

        jda.guilds.forEach {
            val analyticsJobs: MutableMap<String, AnalyticsDataObject> = mutableMapOf()
            val guildId = it.idLong
            val activities = guildActivityService.getGuildActivitiesForGuild(guildId)
            val inviteStatistics = inviteLinkService.getGuildInviteStatistics(guildId)
            val memberStates = memberStatusService.getMemberStatesByGuildId(guildId)
            val voiceStatus = voiceChannelService.getVoiceChannelStatusForGuild(guildId)

            println("## Guild Analytics for $guildId ##")
            if (activities != null) {
                println("## --- Activity Analytics --- ##")
                println(activities.toString())
                analyticsJobs["activity"] = activities
                println("## --- ------------------ --- ##")
            }

            if (inviteStatistics != null) {
                println("## --- Invite Analytics --- ##")
                println(inviteStatistics.toString())
                analyticsJobs["invite"] = inviteStatistics
                println("## --- ---------------- --- ##")
            }

            if (memberStates != null) {
                println("## --- Member States Analytics --- ##")
                println(memberStates.toString())
                analyticsJobs["state"] = memberStates
                println("## --- ----------------------- --- ##")
            }

            if (voiceStatus != null) {
                println("## --- Voice Status Analytics --- ##")
                println(voiceStatus.toString())
                analyticsJobs["voice"] = voiceStatus
                println("## --- ---------------------- --- ##")
            }

            firebaseClient.writeHourlyAnalytics(guildId, analyticsJobs)
            println("## ---------------------------- ##")
        }
    }

    //    @Scheduled(cron = "0 0 12 * * ?")
//    @Scheduled(cron = "0 */2 * ? * *") //2Min
    fun sendDailyAnalytics() {
        println("Daily Analytics Report sending...")
        TimestampUtils.setCurrentTime()

        jda.guilds.forEach {
            val guildId = it.idLong
            val messages = messageService.getMessageInteractionsByGuildId(guildId)

            if(messages != null) {
                println("## --- Guild Message Analytics for: $guildId --- ##")
                println(messages.toString())
                println("## --- ------------------------------------- --- ##")
            }
        }
    }
}