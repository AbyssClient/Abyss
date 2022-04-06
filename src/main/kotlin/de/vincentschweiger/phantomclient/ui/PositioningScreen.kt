package de.vincentschweiger.phantomclient.ui

import de.vincentschweiger.phantomclient.module.ModuleManager
import de.vincentschweiger.phantomclient.module.UIModule
import de.vincentschweiger.phantomclient.utils.client.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import java.awt.Color
import java.awt.geom.Point2D


class PositioningScreen : Screen(LiteralText.EMPTY) {
    private var draggedModule: UIModule? = null

    override fun init() {
        ModuleManager.getUIModules().forEach(::checkOutOfBounds)
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, pTicks: Float) {
        // Render the background
        renderBackground(matrixStack)
        // Draw grid
        for (i in 1..3) {
            drawHorizontalLine(matrixStack, -10, mc.window.width, mc.window.scaledHeight / 4 * i, Color.GRAY.rgb)
            drawVerticalLine(matrixStack, mc.window.scaledWidth / 4 * i, -10, mc.window.height, Color.GRAY.rgb)
        }
        // Render the modules
        ModuleManager.getUIModules().forEach {
            it.render(it.enabled)
        }
    }

    private fun getNearestModule(): UIModule {
        var moduleCoords: MutableMap<Point2D.Double, UIModule> = HashMap()
        ModuleManager.getUIModules().filter { it != draggedModule!! }.forEach { m ->
            moduleCoords[Point2D.Double(m.x, m.y)] = m
        }
        var coords: Set<Point2D.Double> = moduleCoords.keys
        var source = Point2D.Double(draggedModule!!.x, draggedModule!!.y)
        var nearestPoint = coords.first()
        for (coord in coords) {
            val npDist = nearestPoint.distance(source)
            val cDist = coord.distance(source)
            if (npDist > cDist) {
                nearestPoint = coord
            }
        }
        return moduleCoords[nearestPoint]!!
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, distX: Double, distY: Double): Boolean {
        if (draggedModule != null) {
            if (draggedModule!!.getScaledX() + draggedModule!!.width + distX < width
                    && draggedModule!!.getScaledY() + draggedModule!!.height + distY < height
                    && draggedModule!!.getScaledX() + distX >= 0
                    && draggedModule!!.getScaledY() + distY > 0) {
                val nearestModule = getNearestModule()
                var newX = draggedModule!!.getScaledX() + distX
                var newY = draggedModule!!.getScaledY() + distY
                if (newX < (nearestModule.getScaledX() + 6) && newX > (nearestModule.getScaledX() - 6)) {
                    newX = nearestModule.getScaledX()
                }
                draggedModule!!.setPosition(newX, newY)
            }
        }
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

    private fun checkOutOfBounds(m: UIModule) {
        if (m.getScaledX() > width - m.width + 2 || m.getScaledY() > height - m.height + 2 || m.getScaledX() < 0 || m.getScaledY() < 0) m.setPosition(0.0, 0.0)
    }
}