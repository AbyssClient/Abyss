package me.cookie.abyssclient.ui

import me.cookie.abyssclient.render.utils.EmptyScreen
import me.cookie.abyssclient.render.utils.Tessa
import me.cookie.abyssclient.utils.client.mc
import net.minecraft.client.util.math.MatrixStack
import java.awt.Color


class ModuleScreen : EmptyScreen() {
    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        val middleX = mc.window.scaledWidth / 2.0
        val middleY = mc.window.scaledHeight / 2.0
        Tessa.renderRoundedQuad(matrices!!, Color.WHITE, middleX - 150,
            middleY - 100, middleX + 150, middleY + 100, 10.0, 20.0)
        Tessa.renderLine(matrices, Color.DARK_GRAY, middleX - 120, middleY - 100, middleX -120, middleY + 100)
    }
}