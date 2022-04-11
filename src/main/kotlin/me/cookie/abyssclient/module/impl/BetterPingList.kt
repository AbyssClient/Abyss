package me.cookie.abyssclient.module.impl

import com.mojang.blaze3d.systems.RenderSystem
import me.cookie.abyssclient.mixins.gui.PlayerListHudInvoker
import me.cookie.abyssclient.module.Module
import me.cookie.abyssclient.module.impl.PingColors.getColor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.PlayerListHud
import net.minecraft.client.network.PlayerListEntry
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

object BetterPingList : Module("BetterPingList", true){
    val formatString by text("Format String", "%dms")
    val pingBars by boolean("Ping Bars", false)
}

object PingColorUtil {
    fun interpolate(colorStart: Int, colorEnd: Int, offset: Float): Int {
        require(!(offset < 0 || offset > 1)) { "Offset must be between 0.0 and 1.0" }
        val redDiff = getRed(colorEnd) - getRed(colorStart)
        val greenDiff = getGreen(colorEnd) - getGreen(colorStart)
        val blueDiff = getBlue(colorEnd) - getBlue(colorStart)
        val newRed = Math.round(getRed(colorStart) + redDiff * offset)
        val newGreen = Math.round(getGreen(colorStart) + greenDiff * offset)
        val newBlue = Math.round(getBlue(colorStart) + blueDiff * offset)
        return newRed shl 16 or (newGreen shl 8) or newBlue
    }

    fun getRed(color: Int): Int {
        return color shr 16 and 0xFF
    }

    fun getGreen(color: Int): Int {
        return color shr 8 and 0xFF
    }

    fun getBlue(color: Int): Int {
        return color and 0xFF
    }
}


object PingColors {
    const val PING_START = 0
    const val PING_MID = 150
    const val PING_END = 300
    const val COLOR_GREY = 0x535353
    const val COLOR_START = 0x00E676
    const val COLOR_MID = 0xD6CD30
    const val COLOR_END = 0xE53935
    fun getColor(ping: Int): Int {
        if (ping < PING_START) {
            return COLOR_GREY
        }
        return if (ping < PING_MID) {
            PingColorUtil.interpolate(
                COLOR_START,
                COLOR_MID,
                computeOffset(PING_START, PING_MID, ping)
            )
        } else PingColorUtil.interpolate(
            COLOR_MID,
            COLOR_END,
            computeOffset(PING_MID, PING_END, Math.min(ping, PING_END))
        )
    }

    private fun computeOffset(start: Int, end: Int, value: Int): Float {
        val offset = (value - start) / (end - start).toFloat()
        return MathHelper.clamp(offset, 0.0f, 1.0f)
    }
}


object CustomPlayerListHud {
    private const val PING_TEXT_RENDER_OFFSET = -13
    private const val PING_BARS_WIDTH = 11
    fun renderPingDisplay(
        client: MinecraftClient,
        hud: PlayerListHud,
        matrixStack: MatrixStack?,
        width: Int,
        x: Int,
        y: Int,
        player: PlayerListEntry
    ) {
        val textRenderer = client.textRenderer
        val pingString = String.format(BetterPingList.formatString, player.latency)
        val pingStringWidth = textRenderer.getWidth(pingString)
        val pingTextColor = getColor(player.latency)
        var textX = width + x - pingStringWidth + PING_TEXT_RENDER_OFFSET
        if (!BetterPingList.pingBars) {
            textX += PING_BARS_WIDTH
        }

        // Draw the ping text for the given player
        textRenderer.drawWithShadow(matrixStack, pingString, textX.toFloat(), y.toFloat(), pingTextColor)
        if (BetterPingList.pingBars) {
            (hud as PlayerListHudInvoker).invokeRenderLatencyIcon(matrixStack, width, x, y, player)
        } else {
            // If we don't render ping bars, we need to reset the render system color so the rest
            // of the player list renders properly
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        }
    }
}