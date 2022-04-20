package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule
import kotlin.math.roundToInt

object XYZModule : UIModule("XYZ", Category.MISC) {

    override fun render(enabled: Boolean) {
        stack.push()
        if (enabled) {
            val txt: String = getText()
            for ((i, line) in txt.split("\n").withIndex()) {
                mc.textRenderer.drawWithShadow(
                    stack,
                    line,
                    getScaledX().toFloat(),
                    getScaledY().toFloat() + mc.textRenderer.fontHeight * i,
                    color.rgb
                )
            }
        }
        stack.pop()
    }

    override fun getText(): String {
        val x = mc.player?.x?.roundToInt() ?: 0
        val y = mc.player?.y?.roundToInt() ?: 0
        val z = mc.player?.z?.roundToInt() ?: 0
        return "[X]: $x\n" +
                "[Y]: $y\n" +
                "[Z]: $z"
    }

    override val height: Int
        get() = super.height * 3
}