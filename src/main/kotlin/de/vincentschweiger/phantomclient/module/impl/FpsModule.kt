package de.vincentschweiger.phantomclient.module.impl

import de.vincentschweiger.phantomclient.mixins.client.MinecraftClientAccessor
import de.vincentschweiger.phantomclient.module.UIModule

object FpsModule : UIModule("Fps") {
    override fun getText(): String {
        val fps = (mc as MinecraftClientAccessor).currentFps
        return "FPS: $fps"
    }
}