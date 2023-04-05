package com.ccat.operator

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@EnableCaching
@Configuration
class CacheConfiguration {

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager("domains").apply {
        isAllowNullValues = false
        setCaffeine(
            Caffeine.newBuilder().expireAfterAccess(3, TimeUnit.HOURS)
        )
    }
}