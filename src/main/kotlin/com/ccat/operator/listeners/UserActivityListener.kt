package com.ccat.operator.listeners

import com.ccat.operator.model.service.GuildActivityService
import net.dv8tion.jda.api.entities.Activity.ActivityType
import net.dv8tion.jda.api.events.user.UserActivityEndEvent
import net.dv8tion.jda.api.events.user.UserActivityStartEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class UserActivityListener(
    private val activityService: GuildActivityService
): ListenerAdapter() {
    /**
     * TODO: Analytics sent PER HOUR!
     */
    override fun onUserActivityStart(event: UserActivityStartEvent) {
        if(event.user.isBot) return

        val newActivity = event.newActivity
        if(newActivity.type != ActivityType.PLAYING) return

        activityService.updateOrCreateUserActivity(event.guild, event.user.idLong, newActivity)
    }

    override fun onUserActivityEnd(event: UserActivityEndEvent) {
        if(event.user.isBot) return
        if(event.oldActivity.type != ActivityType.PLAYING) return

        activityService.removeUserActivityIfExists(event.guild.idLong, event.user.idLong)
    }
}