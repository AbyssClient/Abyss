package de.vincentschweiger.phantomclient.command.impl

import de.vincentschweiger.phantomclient.command.Command
import de.vincentschweiger.phantomclient.command.CommandCreator
import de.vincentschweiger.phantomclient.command.builder.CommandBuilder
import de.vincentschweiger.phantomclient.utils.client.chat

object HelloCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder
                .begin("hello")
                .handler { command, args -> chat("Hello World!") }
                .build()
    }

}