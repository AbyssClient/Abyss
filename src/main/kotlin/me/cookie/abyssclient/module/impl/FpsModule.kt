package me.cookie.abyssclient.module.impl

import me.cookie.abyssclient.mixins.client.MinecraftClientAccessor
import me.cookie.abyssclient.module.UIModule

object FpsModule : UIModule("Fps") {
    override fun getText(): String {
        val fps = (mc as me.cookie.abyssclient.mixins.client.MinecraftClientAccessor).currentFps
        return "FPS: $fps"
    }
}