package de.vincentschweiger.phantomclient.ui

import de.vincentschweiger.phantomclient.utils.client.mc
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

class ConfigScreen : Screen(LiteralText.EMPTY) {

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        DrawableHelper.fill(matrices, 0, 0, 5 / mc.window.scaledWidth, 5 / mc.window.scaledHeight, -1)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        println("Clicked with $button")
        return super.mouseClicked(mouseX, mouseY, button)
    }
}