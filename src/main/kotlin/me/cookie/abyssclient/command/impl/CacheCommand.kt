package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.server.playerCache
import me.cookie.abyssclient.utils.client.asText
import me.cookie.abyssclient.utils.client.chat

object CacheCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder.begin("cache").hide()
            .subcommand(CommandBuilder.begin("clear").handler { _, _ ->
                playerCache.invalidateAll()
                playerCache.cleanUp()
            }.build())
            .handler { _, _ ->
                val stats = playerCache.stats()
                chat(
                    "Eviction count: ${stats.evictionCount()}\n".asText(),
                    "Hit count: ${stats.hitCount()}\n".asText(),
                    "Load failures: ${stats.loadFailureRate()}\n".asText(),
                    "Estimated Size: ${playerCache.estimatedSize()}".asText(),
                    prefix = false
                )
            }.build()
    }
}