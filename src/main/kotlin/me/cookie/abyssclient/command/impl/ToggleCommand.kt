package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.command.builder.ParameterBuilder
import me.cookie.abyssclient.module.ModuleManager
import me.cookie.abyssclient.utils.client.asText
import me.cookie.abyssclient.utils.client.chat
import net.minecraft.text.TranslatableText

object ToggleCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder.begin("toggle")
            .parameter(
                ParameterBuilder
                    .begin<String>("module")
                    .verifiedBy(ParameterBuilder.STRING_VALIDATOR)
                    .required()
                    .build()
            )
            .handler { cmd, args ->
                val moduleName = args[0] as String
                val module = ModuleManager.getModule(moduleName)
                if (module != null) {
                    module.enabled = !module.enabled
                    if (module.enabled) {
                        chat(TranslatableText( "${cmd.translationBaseKey}.enabled", moduleName))
                    } else {
                        chat(TranslatableText( "${cmd.translationBaseKey}.disabled", moduleName))
                    }
                } else {
                    chat("Module not found".asText())
                }
            }
            .build()
    }
}