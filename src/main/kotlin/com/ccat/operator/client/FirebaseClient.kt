package com.ccat.operator.client

import com.ccat.operator.model.entity.*
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

    fun writeHourlyAnalytics(guildId: Long, analyticsJobs: MutableMap<String, AnalyticsDataObject>) {
        try {
            analyticsJobs.forEach {
                val endpoint = it.key
                val data = it.value
                restTemplate.postForObject("$firebaseApiKey/$endpoint/$guildId.json", data, String::class.java)
            }
        } catch (e: RestClientException) {
            e.printStackTrace()
            throw(e)
        }
    }
}