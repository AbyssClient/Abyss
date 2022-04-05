package de.vincentschweiger.phantomclient.module

import de.vincentschweiger.phantomclient.config.ConfigSystem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText

class PositioningScreen : Screen(LiteralText.EMPTY) {
    private var draggedModule: UIModule? = null

    override fun init() {
        ModuleManager.getUIModules().forEach(::checkOutOfBounds)
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, pTicks: Float) {
        renderBackground(matrixStack)
        ModuleManager.getUIModules().forEach {
            it.render(it.enabled)
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, distX: Double, distY: Double): Boolean {
        if (draggedModule != null) if (draggedModule!!.getScaledX() + draggedModule!!.width + distX < width && draggedModule!!.getScaledY() + draggedModule!!.height + distY < height && draggedModule!!.getScaledX() + distX >= 0 && draggedModule!!.getScaledY() + distY > 0) draggedModule!!.setPosition(draggedModule!!.getScaledX() + distX, draggedModule!!.getScaledY() + distY)
        return super.mouseDragged(mouseX, mouseY, button, distX, distY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // Left click to drag
        // Right click to toggle
        if (button == 0) {
            ModuleManager.getUIModules().forEach { m: UIModule ->
                if (mouseX >= m.getScaledX() && mouseY >= m.getScaledY() && mouseX <= m.getScaledX() + m.width && mouseY <= m.getScaledY() + m.height) draggedModule = m
            }
        } else if (button == 1) {
            ModuleManager.getUIModules().forEach { m: UIModule ->
                if (mouseX >= m.getScaledX() && mouseY >= m.getScaledY() && mouseX <= m.getScaledX() + m.width && mouseY <= m.getScaledY() + m.height) {
                    m.enabled = !m.enabled
                }
            }

        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) draggedModule = null
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun close() {
        super.close()
        ConfigSystem.store()
    }

    private fun checkOutOfBounds(m: UIModule) {
        if (m.getScaledX() > width - m.width + 2 || m.getScaledY() > height - m.height + 2 || m.getScaledX() < 0 || m.getScaledY() < 0) m.setPosition(0.0, 0.0)
    }
}