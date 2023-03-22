package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberStatusService {
    val memberStatus: MutableMap<Long, MemberStatus> = Collections.synchronizedMap(mutableMapOf())

    /**
     * Get the guild specific OnlineStates
     *
     * @param id The Guild-Id.
     * @return A MemberStatus Object containing user amount for each JDA.OnlineStatus
     */
    fun getStatusByGuildId(id: Long): MemberStatus? {
        return memberStatus[id]
    }
}