package de.vincentschweiger.phantomclient.module

import de.vincentschweiger.phantomclient.event.Listenable
import net.minecraft.client.MinecraftClient

open class Module : Listenable {

    var state = true

    val mc: MinecraftClient = MinecraftClient.getInstance()

    open val translationBaseKey: String
        get() = "phantom.module.${getName()}"

    val description: String
        get() = "$translationBaseKey.description"

    open fun getName(): String {
        return ""
    }
}