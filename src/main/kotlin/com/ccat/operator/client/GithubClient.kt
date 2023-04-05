package com.ccat.operator.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class GithubClient(
    @Value("\${discordPhishingLinks.domains}")
    private val baseUrl: String
){
    private val restTemplate = RestTemplate()

    @Cacheable("domains")
    fun retrieveBannedDomains(): String {
        try {
            return restTemplate.getForEntity<String>(baseUrl).body?: throw Exception()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}