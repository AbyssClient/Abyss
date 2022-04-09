package me.cookie.abyssclient.render.ultralight.hooks


import me.cookie.abyssclient.event.*
import me.cookie.abyssclient.render.ultralight.RenderLayer
import me.cookie.abyssclient.render.ultralight.UltralightEngine

/**
 * A integration bridge between Minecraft and Ultralight
 */
object UltralightIntegrationHook : Listenable {

    val gameRenderHandlerHandler = handler<GameRenderEvent> {
        UltralightEngine.update()
    }

    val screenRenderHandler = handler<ScreenRenderEvent> {
        UltralightEngine.render(RenderLayer.SCREEN_LAYER, it.matrices)
    }

    val overlayRenderHandler = handler<OverlayRenderEvent> {
        UltralightEngine.render(RenderLayer.OVERLAY_LAYER, it.matrices)
    }

    val windowResizeWHandler = handler<WindowResizeEvent> {
        UltralightEngine.resize(it.width.toLong(), it.height.toLong())
    }

    val windowFocusHandler = handler<WindowFocusEvent> {
        UltralightEngine.inputAdapter.focusCallback(it.window, it.focused)
    }

    val mouseButtonHandler = handler<MouseButtonEvent> {
        UltralightEngine.inputAdapter.mouseButtonCallback(it.window, it.button, it.action, it.mods)
    }

    val mouseScrollHandler = handler<MouseScrollEvent> {
        UltralightEngine.inputAdapter.scrollCallback(it.window, it.horizontal, it.vertical)
    }

    val mouseCursorHandler = handler<MouseCursorEvent> {
        UltralightEngine.inputAdapter.cursorPosCallback(it.window, it.x, it.y)
    }

    val keyboardKeyHandler = handler<KeyboardKeyEvent> {
        UltralightEngine.inputAdapter.keyCallback(it.window, it.keyCode, it.scancode, it.action, it.mods)
    }

    val keyboardCharHandler = handler<KeyboardCharEvent> {
        UltralightEngine.inputAdapter.charCallback(it.window, it.codepoint)
    }

}