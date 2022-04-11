package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.config.NamedChoice
import me.cookie.abyssclient.module.UIModule
import me.cookie.abyssclient.utils.client.mc
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color
import kotlin.math.max

object KeystrokesModule : UIModule("keystrokes") {

    val mode by enumChoice("mode", KeyStrokesMode.WASD, KeyStrokesMode.values())

    override fun render(enabled: Boolean) {
        stack.push()
        if (enabled) {
            renderKeystrokes(stack)
        } else{
            renderKeystrokes(stack)
        }
        stack.pop()
    }

    private fun renderKeystrokes(matrices: MatrixStack) {
        for (key in mode.getKeys()) {
            val textWidth: Int = mc.textRenderer.getWidth(key.name)
            DrawableHelper.fill(
                matrices,
                (getScaledX() + key.x).toInt(),
                (getScaledY() + key.y).toInt(),
                (getScaledX() + key.x + key.width).toInt(),
                (getScaledY() + key.y + key.height).toInt(),
                if (key.isDown()) Color(255, 255, 255, 102).rgb else Color(0, 0, 0, 102).rgb
            )
            mc.textRenderer.draw(
                matrices,
                key.name,
                (x + key.x + key.width / 2 - textWidth / 2).toFloat(),
                (y + key.y + key.height / 2 - 4).toFloat(),
                getColor(key)
            )
        }
    }

    private fun getColor(key: Key): Int {
        return if (key.isDown()) {
            Color.BLACK.rgb
        } else {
            Color.WHITE.rgb
        }
    }
}


private val W = Key("W", mc.options.forwardKey, 21, 1, 18, 18)
private val A = Key("A", mc.options.leftKey, 1, 21, 18, 18)
private val S = Key("S", mc.options.backKey, 21, 21, 18, 18)
private val D = Key("D", mc.options.rightKey, 41, 21, 18, 18)
private val LMB = Key("LMB", mc.options.attackKey, 1, 41, 28, 18)
private val RMB = Key("RMB", mc.options.useKey, 31, 41, 28, 18)

class Key(
    val name: String,
    val keyBind: KeyBinding,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun isDown(): Boolean {
        return keyBind.isPressed
    }
}

enum class KeyStrokesMode(override val choiceName: String, vararg keys: Key) : NamedChoice {
    WASD("WASD", W, A, S, D);

    var width = 0
    var height = 0
    var nKeys = keys

    fun getKeys(): Array<out Key> {
        return nKeys
    }

    init {
        for (key in keys) {
            width = max(width, key.x + width)
            height = max(height, key.y + height)
        }
    }
}