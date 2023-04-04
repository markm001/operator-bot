package com.ccat.operator.model.service

import com.ccat.operator.model.entity.MemberStatesDto
import com.ccat.operator.utils.TimestampUtils
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import org.springframework.stereotype.Service

@Service
class MemberStatusService {
    fun getMemberStates(members: List<Member>): MemberStatesDto {
        val statusMap = members
            .map { it.onlineStatus }
            .groupingBy { it }
            .eachCount()

        return MemberStatesDto(
            TimestampUtils.getCurrentTime(),
            members.size,
            statusMap[OnlineStatus.ONLINE] ?: 0,
            statusMap[OnlineStatus.IDLE] ?: 0,
            statusMap[OnlineStatus.DO_NOT_DISTURB] ?: 0
        )
    }
}