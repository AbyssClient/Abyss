package me.cookie.abyssclient.render.ultralight.js.bindings

import me.cookie.abyssclient.render.screen.EmptyScreen
import me.cookie.abyssclient.render.ultralight.UltralightEngine
import me.cookie.abyssclient.render.ultralight.theme.ThemeManager
import me.cookie.abyssclient.utils.client.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen
import net.minecraft.client.gui.screen.option.OptionsScreen
import net.minecraft.client.gui.screen.world.SelectWorldScreen
import net.minecraft.client.realms.gui.screen.RealmsMainScreen

/**
 * Referenced by JS as `ui`
 */
object UltralightJsUi {

    // A collection of minecraft screens
    private val _jsScreens = arrayOf(
        JsScreen("title", TitleScreen::class.java) { mc.setScreen(TitleScreen()) },
        JsScreen("singleplayer", SelectWorldScreen::class.java) { mc.setScreen(SelectWorldScreen(it)) },
        JsScreen("multiplayer", MultiplayerScreen::class.java) { mc.setScreen(MultiplayerScreen(it)) },
        JsScreen("options", OptionsScreen::class.java) { mc.setScreen(OptionsScreen(it, mc.options)) },
        JsScreen("language_options", LanguageOptionsScreen::class.java) { mc.setScreen(LanguageOptionsScreen(it, mc.options, mc.languageManager)) },
        JsScreen("multiplayer_realms", RealmsMainScreen::class.java) { mc.setScreen(RealmsMainScreen(it)) }
    )

    fun get(name: String) = _jsScreens.find { it.name == name }
        ?: JsScreen("ultralight", EmptyScreen::class.java) {
            val page = ThemeManager.page(name) ?: error("unknown custom page")
            val emptyScreen = EmptyScreen()
            UltralightEngine.newScreenView(emptyScreen, mc.currentScreen).apply {
                loadPage(page)
            }
            mc.setScreen(emptyScreen)
        }

    fun get(screen: Screen?) = get(screen?.javaClass)

    fun get(clazz: Class<*>?) = _jsScreens.find { it.clazz == clazz }

    fun open(name: String, parent: Screen?) {
        get(name).open(parent)
    }

}

/**
 * A wrapper to make opening screens easier
 */
class JsScreen(val name: String, val clazz: Class<*>, private val execOpen: (Screen?) -> Unit) {
    fun open(parent: Screen?) = execOpen(parent)
}