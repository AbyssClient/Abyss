package de.vincentschweiger.phantomclient.module

import de.vincentschweiger.phantomclient.event.Listenable
import net.minecraft.client.MinecraftClient

open abstract class Module : Listenable {

    var state = true

    val mc: MinecraftClient = MinecraftClient.getInstance()

    abstract fun getName(): String

    open fun init() {}

    open fun initConfig() {}
}