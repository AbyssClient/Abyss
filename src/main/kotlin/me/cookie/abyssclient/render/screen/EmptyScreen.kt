package me.cookie.abyssclient.render.screen

import me.cookie.abyssclient.utils.client.asText
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class EmptyScreen(title: Text = "".asText()) : Screen(title) {

    override fun init() {
        // init nothing
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        // render nothing
    }

}