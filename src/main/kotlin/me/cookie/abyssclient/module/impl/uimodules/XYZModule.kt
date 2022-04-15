package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.UIModule
import kotlin.math.roundToInt

object XYZModule : UIModule("xyz") {
    override fun getText(): String {
        val x = mc.player?.x?.roundToInt() ?: 0
        val y = mc.player?.y?.roundToInt() ?: 0
        val z = mc.player?.z?.roundToInt() ?: 0
        return "[$x,$y,$z]"
    }
}