package com.ccat.operator

import com.ccat.operator.listeners.MemberJoinListener
import com.ccat.operator.listeners.MessageListener
import com.ccat.operator.listeners.StatusListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import xyz.dynxsty.dih4jda.DIH4JDA
import xyz.dynxsty.dih4jda.DIH4JDABuilder

@Configuration
class JdaConfiguration(
    private val env: Environment,
    private val statusListener: StatusListener,
    private val messageListener: MessageListener,
    private val joinListener: MemberJoinListener
) {
    @Bean
    fun build(): JDA {
        val builder = JDABuilder.createDefault(env.getProperty("BOT_TOKEN"))
            .setActivity(Activity.listening("interactions"))
            .setStatus(OnlineStatus.ONLINE)

        //INTENTS:
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS)
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES)
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT)

        //CACHING:
        builder.setMemberCachePolicy(MemberCachePolicy.ALL)
        builder.setChunkingFilter(ChunkingFilter.ALL)
        builder.enableCache(CacheFlag.ONLINE_STATUS)
        builder.enableCache(CacheFlag.ROLE_TAGS)

        //LISTENERS:
        //TODO: replace with Annotation
        builder.addEventListeners(statusListener)
        builder.addEventListeners(messageListener)
        builder.addEventListeners(joinListener)

        return builder.build()
    }

    @Bean
    fun initDih4jda(jda: JDA):DIH4JDA {
        return DIH4JDABuilder
            .setJDA(jda)
            .disableAutomaticCommandRegistration()
            .build()
    }
}