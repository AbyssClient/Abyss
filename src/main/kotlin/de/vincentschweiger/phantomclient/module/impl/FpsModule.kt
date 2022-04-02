package de.vincentschweiger.phantomclient.module.impl

import de.vincentschweiger.phantomclient.mixins.MinecraftClientAccessor
import de.vincentschweiger.phantomclient.module.UIModule

object FpsModule : UIModule() {
    override fun getText(): String {
        val fps = (mc as MinecraftClientAccessor).currentFps
        return "FPS: $fps"
    }

    override fun getName(): String {
        return "FPS"
    }
}