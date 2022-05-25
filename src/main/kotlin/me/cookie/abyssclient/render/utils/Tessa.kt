package me.cookie.abyssclient.render.utils

import com.mojang.blaze3d.systems.RenderSystem
import me.cookie.abyssclient.render.utils.RendererUtils.endRender
import me.cookie.abyssclient.render.utils.RendererUtils.setupRender
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin


/**
 * Helper class for the Tessellator.
 */
object Tessa {
    /**
     * Renders a circle
     *
     * @param matrices The context MatrixStack
     * @param circleColor The color of the circle
     * @param originX The center X coordinate
     * @param originY The center Y coordinate
     * @param radius The radius of the circle
     * @param segments How many segments to render the circle in (between 4 - 360)
     */
    fun renderCircle(matrices: MatrixStack, circleColor: Color, originX: Double, originY: Double, radius: Double, segments: Int) {
        val segs = MathHelper.clamp(segments, 4, 360);
        val color = circleColor.rgb
        val matrix = matrices.peek().positionMatrix
        val f = (color shr 24 and 255).toFloat() / 255.0f
        val g = (color shr 16 and 255).toFloat() / 255.0f
        val h = (color shr 8 and 255).toFloat() / 255.0f
        val k = (color and 255).toFloat() / 255.0f
        val bufferBuilder: BufferBuilder = Tessellator.getInstance().buffer
        RendererUtils.setupRender()
        RenderSystem.setShader(GameRenderer::getPositionColorShader)
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
        var i = 0.0
        do {
            val radians = Math.toRadians(i)
            val sin = sin(radians) * radius
            val cos = cos(radians) * radius
            bufferBuilder.vertex(matrix, (originX + sin).toFloat(), (originY + cos) .toFloat(), 0f).color(g,h,k,f).next()
            i += ((360).toDouble() / segs).coerceAtMost((360 - i))
        } while (i < 360)
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)
        RendererUtils.endRender()
    }

    /**
     * Renders a regular colored quad
     *
     * @param matrices The context MatrixStack
     * @param c        The color of the quad
     * @param x1       The start X coordinate
     * @param y1       The start Y coordinate
     * @param x2       The end X coordinate
     * @param y2       The end Y coordinate
     */
    fun renderQuad(matrices: MatrixStack, c: Color, x1: Double, y1: Double, x2: Double, y2: Double) {
        var x1 = x1
        var y1 = y1
        var x2 = x2
        var y2 = y2
        val color = c.rgb
        var j: Double
        if (x1 < x2) {
            j = x1
            x1 = x2
            x2 = j
        }
        if (y1 < y2) {
            j = y1
            y1 = y2
            y2 = j
        }
        val matrix = matrices.peek().positionMatrix
        val f = (color shr 24 and 255).toFloat() / 255.0f
        val g = (color shr 16 and 255).toFloat() / 255.0f
        val h = (color shr 8 and 255).toFloat() / 255.0f
        val k = (color and 255).toFloat() / 255.0f
        val bufferBuilder = Tessellator.getInstance().buffer
        setupRender()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(matrix, x1.toFloat(), y2.toFloat(), 0.0f).color(g, h, k, f).next()
        bufferBuilder.vertex(matrix, x2.toFloat(), y2.toFloat(), 0.0f).color(g, h, k, f).next()
        bufferBuilder.vertex(matrix, x2.toFloat(), y1.toFloat(), 0.0f).color(g, h, k, f).next()
        bufferBuilder.vertex(matrix, x1.toFloat(), y1.toFloat(), 0.0f).color(g, h, k, f).next()
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)
        endRender()
    }

    private fun renderRoundedQuadInternal(
        matrix: Matrix4f,
        cr: Float,
        cg: Float,
        cb: Float,
        ca: Float,
        fromX: Double,
        fromY: Double,
        toX: Double,
        toY: Double,
        rad: Double,
        samples: Double
    ) {
        val bufferBuilder = Tessellator.getInstance().buffer
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
        val toX1 = toX - rad
        val toY1 = toY - rad
        val fromX1 = fromX + rad
        val fromY1 = fromY + rad
        val map = arrayOf(
            doubleArrayOf(toX1, toY1),
            doubleArrayOf(toX1, fromY1),
            doubleArrayOf(fromX1, fromY1),
            doubleArrayOf(fromX1, toY1)
        )
        for (i in 0..3) {
            val current = map[i]
            val max = 360 / 4.0 + i * 90.0
            var r = i * 90.0
            while (r < max) {
                val rad1 = Math.toRadians(r).toFloat()
                val sin = (Math.sin(rad1.toDouble()) * rad).toFloat()
                val cos = (Math.cos(rad1.toDouble()) * rad).toFloat()
                bufferBuilder.vertex(matrix, current[0].toFloat() + sin, current[1].toFloat() + cos, 0.0f)
                    .color(cr, cg, cb, ca).next()
                r += 90 / samples
            }
            // make sure we render the corner properly by adding one final vertex at the end
            val rad1 = Math.toRadians(max).toFloat()
            val sin = (Math.sin(rad1.toDouble()) * rad).toFloat()
            val cos = (Math.cos(rad1.toDouble()) * rad).toFloat()
            bufferBuilder.vertex(matrix, current[0].toFloat() + sin, current[1].toFloat() + cos, 0.0f)
                .color(cr, cg, cb, ca).next()
        }
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)
    }

    /**
     *
     * Renders a rounded rectangle
     *
     * Best used inside of [MSAAFramebuffer.use]
     *
     * @param matrices The context MatrixStack
     * @param c        The color of the rounded rectangle
     * @param fromX    The start X coordinate
     * @param fromY    The start Y coordinate
     * @param toX      The end X coordinate
     * @param toY      The end Y coordinate
     * @param rad      The radius of the corners
     * @param samples  How many samples to use for the corners
     * @throws IllegalArgumentException If height or width are below 1 px
     */
    fun renderRoundedQuad(
        matrices: MatrixStack,
        c: Color,
        fromX: Double,
        fromY: Double,
        toX: Double,
        toY: Double,
        rad: Double,
        samples: Double
    ) {
        var rad = rad
        val height = toY - fromY
        val width = toX - fromX
        require(height > 0) { "Height should be > 0, got $height" }
        require(width > 0) { "Width should be > 0, got $width" }
        val smallestC = Math.min(height, width) / 2.0
        rad = Math.min(rad, smallestC)
        val color = c.rgb
        val matrix = matrices.peek().positionMatrix
        val f = (color shr 24 and 255).toFloat() / 255.0f
        val g = (color shr 16 and 255).toFloat() / 255.0f
        val h = (color shr 8 and 255).toFloat() / 255.0f
        val k = (color and 255).toFloat() / 255.0f
        setupRender()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples)
        endRender()
    }

    /**
     * Renders a regular line between 2 points
     *
     * @param stack The context MatrixStack
     * @param c     The color of the line
     * @param x     The start X coordinate
     * @param y     The start Y coordinate
     * @param x1    The end X coordinate
     * @param y1    The end Y coordinate
     */
    fun renderLine(stack: MatrixStack, c: Color, x: Double, y: Double, x1: Double, y1: Double) {
        val g = c.red / 255f
        val h = c.green / 255f
        val k = c.blue / 255f
        val f = c.alpha / 255f
        val m = stack.peek().positionMatrix
        val bufferBuilder = Tessellator.getInstance().buffer
        setupRender()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(m, x.toFloat(), y.toFloat(), 0f).color(g, h, k, f).next()
        bufferBuilder.vertex(m, x1.toFloat(), y1.toFloat(), 0f).color(g, h, k, f).next()
        bufferBuilder.end()
        BufferRenderer.draw(bufferBuilder)
        endRender()
    }


}