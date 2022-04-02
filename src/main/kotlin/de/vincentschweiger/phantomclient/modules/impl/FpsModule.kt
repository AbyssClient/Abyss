package de.vincentschweiger.phantomclient.modules.impl

import de.vincentschweiger.phantomclient.mixins.MinecraftClientAccessor
import de.vincentschweiger.phantomclient.modules.UIModule
import net.minecraft.client.MinecraftClient
import java.awt.Color


class ModuleFPS : UIModule() {
    override val text: String
        get() {
            val fps = (MinecraftClient.getInstance() as MinecraftClientAccessor).currentFps
            // FPS: 999
            // 999 FPS
            return if (state == 0) "FPS: $fps" else "$fps FPS"
        }
    override val color: Color
        get() = Color(255, 100, 0)
    override val name: String
        get() = "FPS"
    override val maxState: Int
        get() = 1
}