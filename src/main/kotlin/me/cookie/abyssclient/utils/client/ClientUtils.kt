package me.cookie.abyssclient.utils.client

import net.minecraft.client.MinecraftClient
import net.minecraft.text.BaseText
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import org.slf4j.Logger

// Easy access
val mc = MinecraftClient.getInstance()!!

val logger: Logger
    get() = me.cookie.abyssclient.Abyss.logger


fun dot() = regular(".")

fun regular(text: BaseText) = text.styled { it.withColor(Formatting.GRAY) }

fun regular(text: String) = text.asText().styled { it.withColor(Formatting.GRAY) }

fun variable(text: BaseText) = text.styled { it.withColor(Formatting.DARK_GRAY) }

fun variable(text: String) = text.asText().styled { it.withColor(Formatting.DARK_GRAY) }


/**
 * Open uri in browser
 */
fun browseUrl(url: String) = Util.getOperatingSystem().open(url)