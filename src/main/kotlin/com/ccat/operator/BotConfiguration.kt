package com.ccat.operator

import jakarta.annotation.PostConstruct
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import xyz.dynxsty.dih4jda.DIH4JDA
import xyz.dynxsty.dih4jda.interactions.commands.application.ContextCommand
import xyz.dynxsty.dih4jda.interactions.commands.application.SlashCommand

@Configuration
@ComponentScan(
    includeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [SlashCommand::class, ContextCommand::class, ListenerAdapter::class])]
)
class BotConfiguration(
    private val slashCommands: List<SlashCommand>,
    private val listeners: List<ListenerAdapter>,
    private val diH4JDA: DIH4JDA
) {
    @PostConstruct
    fun initialize() {
        if(slashCommands.isNotEmpty()) {
            diH4JDA.addSlashCommands(*slashCommands.toTypedArray())
        }
        if(listeners.isNotEmpty()) {
            listeners.forEach { diH4JDA.jda.addEventListener(it) }
        }

        diH4JDA.registerInteractions()
    }
}