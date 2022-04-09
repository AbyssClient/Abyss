package me.cookie.abyssclient.render.ultralight.hooks

import me.cookie.abyssclient.event.Listenable
import me.cookie.abyssclient.event.ScreenEvent
import me.cookie.abyssclient.event.handler
import me.cookie.abyssclient.render.screen.EmptyScreen
import me.cookie.abyssclient.render.ultralight.ScreenView
import me.cookie.abyssclient.render.ultralight.UltralightEngine
import me.cookie.abyssclient.render.ultralight.js.bindings.UltralightJsUi
import me.cookie.abyssclient.render.ultralight.theme.ThemeManager
import me.cookie.abyssclient.utils.client.mc
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