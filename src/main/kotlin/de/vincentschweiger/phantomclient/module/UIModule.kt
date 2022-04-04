package de.vincentschweiger.phantomclient.module

import de.vincentschweiger.phantomclient.Phantom
import de.vincentschweiger.phantomclient.config.ConfigOpt
import de.vincentschweiger.phantomclient.event.OverlayRenderEvent
import de.vincentschweiger.phantomclient.event.handler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import java.awt.Color

abstract class UIModule : Module() {
    @ConfigOpt
    private var x = 0.0
    @ConfigOpt
    private var y = 0.0
    var stack = MatrixStack()

    val renderHandler = handler<OverlayRenderEvent> {
        if ((mc.currentScreen == null || mc.currentScreen is ChatScreen || mc.currentScreen is InventoryScreen) && !mc.options.debugEnabled)
            if (state) render(state)
    }

    fun getX(): Double {
        return x * MinecraftClient.getInstance().window.scaledWidth
    }

    fun getY(): Double {
        return y * MinecraftClient.getInstance().window.scaledHeight
    }

    fun setPosition(x: Double, y: Double) {
        this.x = x / MinecraftClient.getInstance().window.scaledWidth
        this.y = y / MinecraftClient.getInstance().window.scaledHeight
    }

    open val color: Color = Color.WHITE

    fun render(enabled: Boolean) {
        stack.push()
        if (enabled) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, getText(), getX().toFloat(), getY().toFloat(), color.rgb)
        } else {
            //If you don't want to render the disabled modules in the drag-screen, just remove/comment following line
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, LiteralText(getText()).setStyle(Style.EMPTY.withStrikethrough(true)), getX().toFloat(), getY().toFloat(), color.rgb)
        }
        stack.pop()
    }

    open val width: Int
        get() = MinecraftClient.getInstance().textRenderer.getWidth(getText())
    open val height: Int
        get() = 9


    abstract fun getText(): String
}