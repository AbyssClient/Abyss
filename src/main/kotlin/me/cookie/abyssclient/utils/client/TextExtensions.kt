package me.cookie.abyssclient.utils.client


import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import java.util.regex.Pattern

private val COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]")

fun String.stripMinecraftColorCodes(): String {
    return COLOR_PATTERN.matcher(this).replaceAll("")
}

fun text() = LiteralText("")

fun String.asText() = LiteralText(this)

fun Text.outputString(): String = "${asString()}${siblings.joinToString(separator = "") { it.outputString() }}"

/**
 * Translate alt color codes to minecraft color codes
 */
fun String.translateColorCodes(): String {
    val charset = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr"

    val chars = toCharArray()
    for (i in 0 until chars.size - 1) {
        if (chars[i] == '&' && charset.contains(chars[i + 1], true)) {
            chars[i] = '§'
            chars[i + 1] = chars[i + 1].lowercaseChar()
        }
    }

    return String(chars)
}

fun String.toLowerCamelCase() = this.replaceFirst(this.toCharArray()[0], this.toCharArray()[0].lowercaseChar())