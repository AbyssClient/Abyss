package de.vincentschweiger.phantomclient.utils.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Window

// Global minecraft timer
object Timer {
    var timerSpeed = 1f
}

val MinecraftClient.timer
    get() = Timer

val Window.size
    get() = Pair(width, height)

val Window.longedSize
    get() = Pair(width.toLong(), height.toLong())