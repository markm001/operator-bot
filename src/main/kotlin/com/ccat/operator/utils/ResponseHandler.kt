package com.ccat.operator.utils

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction

object ResponseHandler {
    fun missingArgs(event: IReplyCallback): ReplyCallbackAction {
        return event.reply("Error. Command requires valid Arguments.")
    }

    fun memberNotFound(event: IReplyCallback, member: Member): ReplyCallbackAction {
        return event.reply(
            "The target Member: ${member.effectiveName}/${member.idLong} was not found."
        )
    }

    fun missingPermissions(event: IReplyCallback): ReplyCallbackAction {
        return event.reply(
            "Error. You're missing the necessary permissions to execute this command."
        )
    }
}