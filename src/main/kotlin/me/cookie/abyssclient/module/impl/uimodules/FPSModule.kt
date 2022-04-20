package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule

object FPSModule : UIModule("FPS", Category.MISC) {
    override fun getText(): String {
        val fps = (mc as me.cookie.abyssclient.mixins.client.MinecraftClientAccessor).currentFps
        return "FPS: $fps"
    }
}