package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.server.SocketClient
import me.cookie.abyssclient.server.getPlayer
import me.cookie.abyssclient.utils.client.chat
import me.cookie.abyssclient.utils.client.mc

object HelloCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder
            .begin("hello")
            .handler { _, _ ->
                chat("Hello World!")
                val world = mc.world
                val players = world!!.players!!
                for (player in players) {
                    val playerObj = getPlayer(player.uuidAsString)
                    println(playerObj?.uuid ?: "player is null")
                }
            }
            .build()
    }

}