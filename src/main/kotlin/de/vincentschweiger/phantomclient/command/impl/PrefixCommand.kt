package de.vincentschweiger.phantomclient.command.impl

import de.vincentschweiger.phantomclient.command.Command
import de.vincentschweiger.phantomclient.command.CommandCreator
import de.vincentschweiger.phantomclient.command.CommandManager
import de.vincentschweiger.phantomclient.command.builder.CommandBuilder
import de.vincentschweiger.phantomclient.command.builder.ParameterBuilder
import de.vincentschweiger.phantomclient.utils.client.chat
import net.minecraft.text.TranslatableText

object PrefixCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder.begin("prefix")
                .parameter(
                        ParameterBuilder
                                .begin<String>("prefix")
                                .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                                .required()
                                .build()
                )
                .handler { _, args ->
                    val newPrefix = args[0] as String
                    CommandManager.Options.prefix = newPrefix
                    chat(TranslatableText("phantom.commands.prefixSet", newPrefix))
                }
                .build()
    }
}