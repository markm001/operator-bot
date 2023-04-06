package com.ccat.operator.schedulerjobs

import com.ccat.operator.client.FirebaseClient
import com.ccat.operator.model.service.GuildAnalyticsService
import com.ccat.operator.utils.TimestampUtils
import io.github.oshai.KotlinLogging
import net.dv8tion.jda.api.JDA
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class SendAnalyticsJob(
    private val jda: JDA,
    private val analyticsService: GuildAnalyticsService,
    private val firebaseClient: FirebaseClient
) {
    private val logger = KotlinLogging.logger{}

    @Scheduled(cron = "0 0 * ? * *")
    fun sendHourlyAnalytics() {
        logger.info { "Hourly Analytics Report sending..." }
        TimestampUtils.setCurrentTime()

        jda.guilds.forEach {
            val analyticsSnapshot = analyticsService.getGuildSnapshot(it)
            firebaseClient.writeHourlyAnalytics(it.idLong, analyticsSnapshot)
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    fun sendDailyAnalytics() {
        logger.info { "Daily Analytics Report sending..." }
        TimestampUtils.setCurrentTime()

        jda.guilds.forEach {
            val guildId = it.idLong
            val messageAnalytics = analyticsService.getMessageAnalytics(guildId)?: return@forEach

            firebaseClient.sendDailyAnalytics(guildId, messageAnalytics)
        }
    }
}