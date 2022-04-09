package me.cookie.abyssclient.utils.client

import me.cookie.abyssclient.Phantom
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

private val clientPrefix = "§8[§9§l${me.cookie.abyssclient.Phantom.CLIENT_NAME}§8] ".asText()

fun chat(vararg texts: Text, prefix: Boolean = true) {
    val literalText = if (prefix) clientPrefix.copy() else LiteralText("")
    texts.forEach { literalText.append(it) }

    if (mc.player == null) {
        logger.info("(Chat) ${literalText.outputString()}")
        return
    }

    mc.inGameHud.chatHud.addMessage(literalText)
}

fun chat(text: String) = chat(text.asText())
