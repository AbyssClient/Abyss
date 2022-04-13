package me.cookie.abyssclient.cosmetics

import me.cookie.abyssclient.event.Listenable
import me.cookie.abyssclient.event.PlayerJoinWorldEvent
import me.cookie.abyssclient.event.handler
import me.cookie.abyssclient.server.getPlayer

object Cosmetic : Listenable {
    val playerJoinHandler = handler<PlayerJoinWorldEvent> { event ->
        getPlayer(event.player.uuidAsString)
    }
}