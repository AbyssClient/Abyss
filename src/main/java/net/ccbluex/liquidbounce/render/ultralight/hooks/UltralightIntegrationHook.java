package net.ccbluex.liquidbounce.render.ultralight.hooks;

import de.vincentschweiger.phantomclient.events.EventAnnotation;
import de.vincentschweiger.phantomclient.events.impl.*;
import net.ccbluex.liquidbounce.render.ultralight.RenderLayer;
import net.ccbluex.liquidbounce.render.ultralight.UltralightEngine;

public class UltralightIntegrationHook {

    @EventAnnotation
    public void onKeyboardKey(KeyboardKeyEvent it) {
        UltralightEngine.inputAdapter.keyCallback(it.getWindow(), it.getKey(), it.getScancode(), it.getI(), it.getJ());
    }

    @EventAnnotation
    public void onKeyboardChar(KeyboardCharEvent event) {
        UltralightEngine.inputAdapter.charCallback(event.getWindow(), event.getI());
    }

    @EventAnnotation
    public void onMouseScrool(MouseScrollEvent event) {
        UltralightEngine.inputAdapter.scrollCallback(event.getWindow(), event.getHorizontal(), event.getVertical());
    }

    @EventAnnotation
    public void onMouseCursor(MouseCursorEvent event) {
        UltralightEngine.inputAdapter.cursorPosCallback(event.getWindow(), event.getX(), event.getY());
    }

    @EventAnnotation
    public void onRenderGameOverlay(RenderOverlayEvent event) {
        UltralightEngine.INSTANCE.render(RenderLayer.OVERLAY_LAYER, event.getMatrixStack());
    }

    @EventAnnotation
    public void onRenderScreen(ScreenRenderEvent event) {
        UltralightEngine.INSTANCE.render(RenderLayer.SCREEN_LAYER, event.getMatrixStack());
    }

    @EventAnnotation
    public void onGameRender(GameRenderEvent event) {
        UltralightEngine.INSTANCE.update();
    }

    @EventAnnotation
    public void onWindowResize(WindowResizeEvent event) {
        UltralightEngine.INSTANCE.resize(event.getWidth(), event.getHeight());
    }

    @EventAnnotation
    public void onWindowFocus(WindowFocusEvent event) {
        UltralightEngine.inputAdapter.focusCallback(event.getWindow(), event.isFocused());
    }

    @EventAnnotation
    public void onMouseButton(MouseButtonEvent event) {
        UltralightEngine.inputAdapter.mouseButtonCallback(event.getWindow(), event.getButton(), event.getAction(), event.getMods());
    }
}
