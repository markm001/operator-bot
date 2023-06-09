package com.ccat.operator.model.service

import com.ccat.operator.model.entity.AnalyticsDataObject
import com.ccat.operator.model.entity.MessageInteractionsDto
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import org.springframework.stereotype.Service

@Service
class GuildAnalyticsService(
    private val jda: JDA,

    private val activityService: GuildActivityService,
    private val memberService: MemberStatusService,
    private val voiceService: VoiceChannelService,
    private val inviteService: InviteLinkService,
    private val messageService: MessageService
) {
    /**
     * Retrieves a Snapshot of the current Analytics for the specified Guild
     *
     * @param guild The JDA.Guild
     * @return A map relating the API endpoint name and AnalyticsObject for Firebase
     */
    fun getGuildSnapshot(guild: Guild): MutableMap<String, AnalyticsDataObject> {
        val analyticsJobs: MutableMap<String, AnalyticsDataObject> = mutableMapOf()

        val members = guild.members.filter { member -> member.idLong != jda.selfUser.idLong }
        val memberStates = memberService.getMemberStates(members)
        analyticsJobs["state"] = memberStates

        val activities: Map<Long, Activity> = activityService.getActivitiesForAllGuildMembers(guild)
        val memberActivities = activityService.mapToMemberActivity(activities)
        if(memberActivities != null) {
            analyticsJobs["activity"] = memberActivities
        }

        val inviteLinkUses = inviteService.getGuildInviteStatistics(guild.idLong)
        if(inviteLinkUses != null) {
            analyticsJobs["invite"] = inviteLinkUses
        }

        val voiceChannelActivity = voiceService.getVoiceChannelActivity(guild)
        val voiceStates = voiceService.mapToVoiceChannelStatus(voiceChannelActivity)
        if(voiceStates != null) {
            analyticsJobs["voice"] = voiceStates
        }

        return analyticsJobs
    }

    /**
     * Retrieves, then flushes all Message Interactions for the specified GuildId
     *
     * @param guildId The Id for the JDA.Guild
     * @return Object containing Message Interactions for the current time.
     */
    fun getMessageAnalytics(guildId: Long): MessageInteractionsDto? {
        val interactions = messageService.getMessageInteractionsByGuildId(guildId)
        messageService.flushMessageInteractions(guildId)

        return interactions
    }
}