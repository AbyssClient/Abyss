package de.vincentschweiger.phantomclient.modules

import de.vincentschweiger.phantomclient.event.OverlayRenderEvent
import de.vincentschweiger.phantomclient.event.handler
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import java.awt.Color

abstract class UIModule : Module() {
    private var x = 0.0
    private var y = 0.0
    var state = 0
        private set
    open val maxState = 0
    var stack = MatrixStack()

    val renderHandler = handler<OverlayRenderEvent> {
        render(isEnabled)
    }

    fun render(enabled: Boolean) {
        stack.push()
        if (enabled) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, text, getX().toFloat(), getY().toFloat(), color.rgb)
        } else {
            //If you don't want to render the disabled modules in the drag-screen, just remove/comment following line
            MinecraftClient.getInstance().textRenderer.drawWithShadow(stack, LiteralText(text).setStyle(Style.EMPTY.withStrikethrough(true)), getX().toFloat(), getY().toFloat(), color.rgb)
        }
        stack.pop()
    }

    open val text: String?
        get() = ""
    open val color: Color
        get() = Color.WHITE

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

    val width: Int
        get() = MinecraftClient.getInstance().textRenderer.getWidth(text)
    val height: Int
        get() = 9

    override fun load() {
        super.load()
        setPosition(0.0, 0.0)
    }

    fun incrState() {
        if (state < maxState) state++ else state = 0
    }
}