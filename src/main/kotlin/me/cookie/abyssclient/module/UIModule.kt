package me.cookie.abyssclient.module

import me.cookie.abyssclient.event.OverlayRenderEvent
import me.cookie.abyssclient.event.handler
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import java.awt.Color


abstract class UIModule(
    name: String,
    state: Boolean = false,
) : Module(name, state) {
    var x by double("x", 0.0)
    var y by double("y", 0.0)

    var stack = MatrixStack()

    val renderHandler = handler<OverlayRenderEvent> {
        if (
            (
                    mc.currentScreen == null
                            || mc.currentScreen is ChatScreen
                            || mc.currentScreen is InventoryScreen
                    )
            && !mc.options.debugEnabled
        ) {
            if (enabled) render(enabled)
        }
    }

    fun getScaledX(): Double {
        return x * mc.window.scaledWidth
    }

    fun getScaledY(): Double {
        return y * mc.window.scaledHeight
    }

    fun setPosition(x: Double, y: Double) {
        this.x = x / mc.window.scaledWidth
        this.y = y / mc.window.scaledHeight
    }

    open val color: Color = Color.WHITE

    fun render(enabled: Boolean) {
        stack.push()
        if (enabled) {
            mc.textRenderer.drawWithShadow(stack, getText(), getScaledX().toFloat(), getScaledY().toFloat(), color.rgb)
        } else {
            // If you don't want to render the disabled modules in the drag-screen, just remove/comment following line
            mc.textRenderer.drawWithShadow(
                stack,
                LiteralText(getText()).setStyle(Style.EMPTY.withStrikethrough(true)),
                getScaledX().toFloat(),
                getScaledY().toFloat(),
                color.rgb
            )
        }
        stack.pop()
    }

    open val width: Int
        get() = mc.textRenderer.getWidth(getText())
    open val height: Int
        get() = 9

    open fun getText(): String {
        return "dummy"
    }
}