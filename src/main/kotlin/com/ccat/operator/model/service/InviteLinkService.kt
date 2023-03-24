package com.ccat.operator.model.service

import org.springframework.stereotype.Service

@Service
class InviteLinkService {
    private val initialInvites: MutableMap<Long, MutableMap<String, Int>> = mutableMapOf()
    private val inviteList: MutableMap<Long, MutableMap<Long, String>> = mutableMapOf()

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
     * Increment Invite-Link uses per UserID (duplicate Users merged)
     */
    fun incrementInviteLinkUses(guildId: Long, code: String, userId: Long) {
        inviteList.getOrPut(guildId) { mutableMapOf() }[userId] = code
    }

    /**
     * Get the uses for each Invite-Link of the GuildID
     *
     * @return Map of InviteCode and real uses
     */
    private fun getInviteLinkUses(guildId: Long):Map<String, Int> {
        return inviteList[guildId]!!.values.groupBy { it }.mapValues { it.value.size }
    }
}