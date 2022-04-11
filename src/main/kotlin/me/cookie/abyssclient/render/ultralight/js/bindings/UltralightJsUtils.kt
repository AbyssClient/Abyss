package me.cookie.abyssclient.render.ultralight.js.bindings

import me.cookie.abyssclient.utils.client.browseUrl
import net.minecraft.client.MinecraftClient

/**
 * Referenced by JS as `utils`
 */
object UltralightJsUtils {

    /**
     * Open link
     */
    fun browse(url: String) = browseUrl(url)

    /**
     * Stop minecraft
     */
    fun scheduleStop() {
        MinecraftClient.getInstance().scheduleStop()
    }
}