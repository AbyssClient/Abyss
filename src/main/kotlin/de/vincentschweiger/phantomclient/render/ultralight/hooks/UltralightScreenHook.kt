package de.vincentschweiger.phantomclient.render.ultralight.hooks

import de.vincentschweiger.phantomclient.event.Listenable
import de.vincentschweiger.phantomclient.event.ScreenEvent
import de.vincentschweiger.phantomclient.event.handler
import de.vincentschweiger.phantomclient.render.screen.EmptyScreen
import de.vincentschweiger.phantomclient.render.ultralight.ScreenView
import de.vincentschweiger.phantomclient.render.ultralight.UltralightEngine
import de.vincentschweiger.phantomclient.render.ultralight.js.bindings.UltralightJsUi
import de.vincentschweiger.phantomclient.render.ultralight.theme.ThemeManager
import de.vincentschweiger.phantomclient.utils.client.mc
import net.minecraft.client.gui.screen.TitleScreen

object UltralightScreenHook : Listenable {

    /**
     * Handle opening new screens
     */
    val screenHandler = handler<ScreenEvent> { event ->
        UltralightEngine.cursorAdapter.unfocus()

        val activeView = UltralightEngine.activeView
        if (activeView is ScreenView) {
            if (activeView.context.events._fireViewClose()) {
                UltralightEngine.removeView(activeView)
            }
        }

        val screen = event.screen ?: if (mc.world != null) return@handler else TitleScreen()
        val name = UltralightJsUi.get(screen)?.name ?: return@handler
        val page = ThemeManager.page(name) ?: return@handler

        val emptyScreen = EmptyScreen()
        UltralightEngine.newScreenView(emptyScreen, adaptedScreen = screen, parentScreen = mc.currentScreen).apply {
            loadPage(page)
        }

        mc.setScreen(emptyScreen)
        event.cancelEvent()
    }

}