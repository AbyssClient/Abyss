package me.cookie.abyssclient.command.impl

import me.cookie.abyssclient.command.Command
import me.cookie.abyssclient.command.CommandCreator
import me.cookie.abyssclient.command.CommandException
import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.command.builder.CommandBuilder
import me.cookie.abyssclient.command.builder.ParameterBuilder
import me.cookie.abyssclient.utils.client.asText
import me.cookie.abyssclient.utils.client.chat
import me.cookie.abyssclient.utils.client.regular
import me.cookie.abyssclient.utils.client.variable
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting
import kotlin.math.ceil
import kotlin.math.roundToInt

object HelpCommand : CommandCreator {
    override fun createCommand(): Command {
        return CommandBuilder.begin("help")
            .parameter(
                ParameterBuilder.begin<Int>("page")
                    .verifiedBy(ParameterBuilder.INTEGER_VALIDATOR)
                    .optional()
                    .build()
            )
            .handler { command, args ->
                val page = if (args.size > 1) {
                    args[0] as Int
                } else {
                    1
                }.coerceAtLeast(1)

                val commands = CommandManager.sortedBy { it.name }.filter { !it.hidden }

                // Max page
                val maxPage = ceil(commands.size / 8.0).roundToInt()
                if (page > maxPage) {
                    throw CommandException(command.result("pageNumberTooLarge", maxPage))
                }

                // Print out help page
                chat(command.result("help").styled { it.withColor(Formatting.RED).withBold(true) })
                chat(regular(command.result("pageCount", variable("$page / $maxPage"))))

                val iterPage = 8 * page
                for (cmd in commands.subList(iterPage - 8, iterPage.coerceAtMost(commands.size))) {
                    val aliases = LiteralText("")

                    if (cmd.aliases.isNotEmpty()) {
                        cmd.aliases.forEach { alias -> aliases.append(variable(", ")).append(regular(alias)) }
                    }

                    chat(
                        "- ".asText()
                            .styled { it.withColor(Formatting.BLUE) }
                            .append(CommandManager.Options.prefix + cmd.name)
                            .styled { it.withColor(Formatting.GRAY) }
                            .append(aliases)
                    )
                }

                chat(
                    "--- ".asText()
                        .styled { it.withColor(Formatting.DARK_GRAY) }
                        .append(variable("${CommandManager.Options.prefix}help <"))
                        .append(variable(command.result("page")))
                        .append(variable(">"))
                )
            }
            .build()
    }
}