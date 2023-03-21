package com.ccat.operator.utils

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction

class ReplyResponse {
    companion object {
        fun missingArgs(event: IReplyCallback):ReplyCallbackAction {
            return event.reply("Error. Command requires valid Arguments.")
        }
    }
}