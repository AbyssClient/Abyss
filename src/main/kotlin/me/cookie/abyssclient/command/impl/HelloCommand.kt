package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.utils.client.chat

object HelloCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder
            .begin("hello")
            .handler { command, args -> chat("Hello World!") }
            .build()
    }

}