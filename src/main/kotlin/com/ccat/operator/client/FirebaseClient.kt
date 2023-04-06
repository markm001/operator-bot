package com.ccat.operator.client

import com.ccat.operator.model.entity.*
import com.ccat.operator.utils.ErrorLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class FirebaseClient(
    @Value("\${firebase.api-key}")
    private val firebaseApiKey: String
) {
    val restTemplate = RestTemplate()

    /**
     * Writes the Hourly Analytics to the Database
     *
     * @param guildId The Id of the JDA.Guild
     * @param analyticsJobs A map relating the endpoint and Object containing the Analytics Data
     */
    fun writeHourlyAnalytics(guildId: Long, analyticsJobs: Map<String, AnalyticsDataObject>) {
        try {
            analyticsJobs.forEach {
                val endpoint = it.key
                val data = it.value
                restTemplate.postForObject("$firebaseApiKey/$endpoint/$guildId.json", data, String::class.java)
            }
        } catch (e: RestClientException) {
            ErrorLogger.catch(e)
            throw (e)
        }
    }

    /**
     * Writes the Daily Analytics to the Database
     *
     * @param guildId The Id of the JDA.Guild
     * @param messageAnalytics The Object containing all Message Interactions for the specific day
     */
    fun sendDailyAnalytics(guildId: Long, messageAnalytics: MessageInteractionsDto) {
        try {
            restTemplate.postForObject("$firebaseApiKey/message/$guildId.json", messageAnalytics, String::class.java)
        } catch (e: RestClientException) {
            e.printStackTrace()
            ErrorLogger.catch(e)
            throw (e)
        }
    }
}