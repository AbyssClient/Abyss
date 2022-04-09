package me.cookie.abyssclient.utils.client

import me.cookie.abyssclient.Phantom
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Util
import org.slf4j.Logger

// Easy access
val mc = MinecraftClient.getInstance()!!

val logger: Logger
    get() = me.cookie.abyssclient.Phantom.logger

/**
 * Open uri in browser
 */
fun browseUrl(url: String) = Util.getOperatingSystem().open(url)