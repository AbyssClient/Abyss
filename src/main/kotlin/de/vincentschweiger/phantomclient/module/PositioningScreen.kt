package de.vincentschweiger.phantomclient.module

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
            it.render(it.state)
        }
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, distX: Double, distY: Double): Boolean {
        if (draggedModule != null) if (draggedModule!!.getX() + draggedModule!!.width + distX < width && draggedModule!!.getY() + draggedModule!!.height + distY < height && draggedModule!!.getX() + distX >= 0 && draggedModule!!.getY() + distY > 0) draggedModule!!.setPosition(draggedModule!!.getX() + distX, draggedModule!!.getY() + distY)
        return super.mouseDragged(mouseX, mouseY, button, distX, distY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // Left click to drag
        // Right click to toggle
        if (button == 0) {
            ModuleManager.getUIModules().forEach { m: UIModule ->
                if (mouseX >= m.getX() && mouseY >= m.getY() && mouseX <= m.getX() + m.width && mouseY <= m.getY() + m.height) draggedModule = m
            }
        } else if (button == 1) {

            ModuleManager.getUIModules().forEach { m: UIModule ->
                if (mouseX >= m.getX() && mouseY >= m.getY() && mouseX <= m.getX() + m.width && mouseY <= m.getY() + m.height) {
                    m.state = !m.state
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
        //getRegisteredModules().forEach(Consumer { obj: UIModule -> obj.save() })
    }

    private fun checkOutOfBounds(m: UIModule) {
        if (m.getX() > width - m.width + 2 || m.getY() > height - m.height + 2 || m.getX() < 0 || m.getY() < 0) m.setPosition(0.0, 0.0)
    }
}