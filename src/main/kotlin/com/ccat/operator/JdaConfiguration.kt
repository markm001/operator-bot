package com.ccat.operator

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import xyz.dynxsty.dih4jda.DIH4JDA
import xyz.dynxsty.dih4jda.DIH4JDABuilder

@Configuration
class JdaConfiguration(
    private val env: Environment

) {

    @Bean
    fun build(): JDA {
        val builder = JDABuilder.createDefault(env.getProperty("BOT_TOKEN"))
            .setStatus(OnlineStatus.ONLINE)

        //INTENTS:
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS)
        builder.enableIntents(GatewayIntent.GUILD_PRESENCES)
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT)

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