package com.ccat.operator.model.service

import com.ccat.operator.model.entity.InviteStatisticsResponse
import com.ccat.operator.model.entity.InviteUser
import org.springframework.stereotype.Service

@Service
class InviteLinkService {
    private val initialInvites: MutableMap<Long, MutableMap<String, Int>> = mutableMapOf()
    private val inviteUsers: MutableSet<InviteUser> = mutableSetOf()

    /**
     * Checks if User has used the Link before & creates Object containing userID, guildID, inviteCode
     * Duplicate users for guild are discarded.
     */
    fun saveInviteUser(guildId: Long, userId:Long, inviteCode:String) {
        val res = inviteUsers.filter { it.guildId == guildId && it.userId == userId }

        if(res.isNotEmpty()) { return }

        inviteUsers.add( InviteUser(
            userId,
            guildId,
            inviteCode
        ))
    }

    /**
     * Initializes all available Invite-Links and current uses for GuildID
     */
    fun initGuildInviteUses(guildId: Long, invites:Map<String, Int>) {
        initialInvites[guildId] = invites.toMutableMap()
    }

    /**
     * Checks all invites and check which value was incremented, update the initial value
     *
     * @return The used invite Code used
     */
    fun getUsedLinkAndIncrement(guildId: Long, invites: Map<String, Int>): String? {
        val invitesForGuildId = initialInvites[guildId]!!

        val updatedInvite = invitesForGuildId.filter { (k, v) -> invites[k] != v }
        if(updatedInvite.isNotEmpty()) {
            val inviteCode = updatedInvite.keys.first()

            invitesForGuildId[inviteCode] = updatedInvite[inviteCode]!!.inc()
            return inviteCode
        }

        return null
    }

    /**
     * Sum Users for each Invite-Link - Get the uses for each Link of the specified GuildID
     *
     * @param guildId The Id of the Guild.
     * @return Object with InviteCode and real uses
     */
    fun getGuildInviteStatistics(guildId: Long): List<InviteStatisticsResponse> {
        return inviteUsers
            .filter { it.guildId == guildId }
            .groupBy { it.inviteCode }
            .map { (k, v) -> InviteStatisticsResponse(k, v.size) }
    }
}