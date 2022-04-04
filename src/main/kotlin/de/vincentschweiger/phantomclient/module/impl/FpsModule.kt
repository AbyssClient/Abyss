package de.vincentschweiger.phantomclient.module.impl

import de.vincentschweiger.phantomclient.Phantom
import de.vincentschweiger.phantomclient.config.ConfigOpt
import de.vincentschweiger.phantomclient.mixins.MinecraftClientAccessor
import de.vincentschweiger.phantomclient.module.UIModule

object FpsModule : UIModule() {

    @ConfigOpt
    var s: String = "default"

    override fun getText(): String {
        val fps = (mc as MinecraftClientAccessor).currentFps
        return "FPS: $fps"
    }

    override fun getName(): String {
        return "FPS"
    }

    override fun initConfig() {
        Phantom.config.register(this)
    }
}