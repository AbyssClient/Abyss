package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.server.SocketClient
import me.cookie.abyssclient.server.getPlayer
import me.cookie.abyssclient.server.playerCache
import me.cookie.abyssclient.utils.client.asText
import me.cookie.abyssclient.utils.client.chat

object CacheStatus : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder.begin("cache").hide().handler { _, _ ->
                val stats = playerCache.stats()
                chat(
                    "Eviction count: ${stats.evictionCount()}".asText(),
                    "Hit count: ${stats.hitCount()}".asText(),
                    "Load failures: ${stats.loadFailureRate()}".asText(),
                    "Estimated Size: ${playerCache.estimatedSize()}".asText(),
                    prefix = false
                )
            }.build()
    }
}