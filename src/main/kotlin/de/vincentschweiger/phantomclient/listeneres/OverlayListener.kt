package de.vincentschweiger.phantomclient.listeneres

import de.vincentschweiger.phantomclient.event.Listenable
import de.vincentschweiger.phantomclient.event.OverlayRenderEvent
import de.vincentschweiger.phantomclient.event.handler
import de.vincentschweiger.phantomclient.modules.Modules
import de.vincentschweiger.phantomclient.utils.client.mc
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen

object OverlayListener : Listenable {
    val renderHandler = handler<OverlayRenderEvent> {
        println("OverlayRenderEvent")
        val curScreen = mc.currentScreen ?: return@handler
        if (curScreen is InventoryScreen) return@handler
        if (curScreen is ChatScreen) return@handler
        Modules.getRegisteredModules().forEach{
            if (it.isEnabled) it.render(true)
        }
    }
}