package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.command.builder.ParameterBuilder
import me.cookie.abyssclient.utils.client.chat
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