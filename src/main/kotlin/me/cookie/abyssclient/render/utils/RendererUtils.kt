package me.cookie.abyssclient.render.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import org.lwjgl.BufferUtils
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import javax.imageio.ImageIO


object RendererUtils {
    fun setupRender() {
        RenderSystem.disableCull()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    }

    fun endRender() {
        RenderSystem.disableBlend()
        RenderSystem.enableCull()
    }

    /**
     *
     * Registers a BufferedImage as Identifier, to be used in future render calls
     *
     * **WARNING:** This will wait for the main tick thread to register the texture, keep in mind that the texture will not be available instantly
     *
     * **WARNING 2:** This will throw an exception when called when the OpenGL context is not yet made
     *
     * @param i  The identifier to register the texture under
     * @param bi The BufferedImage holding the texture
     */
    fun registerBufferedImageTexture(i: Identifier?, bi: BufferedImage?) {
        try {
            val baos = ByteArrayOutputStream()
            ImageIO.write(bi, "png", baos)
            val bytes: ByteArray = baos.toByteArray()
            val data: ByteBuffer = BufferUtils.createByteBuffer(bytes.size).put(bytes)
            data.flip()
            val tex = NativeImageBackedTexture(NativeImage.read(data))
            MinecraftClient.getInstance().execute {
                MinecraftClient.getInstance().textureManager.registerTexture(i, tex)
            }
        } catch (e: Exception) { // should never happen, but just in case
            e.printStackTrace()
        }
    }
}