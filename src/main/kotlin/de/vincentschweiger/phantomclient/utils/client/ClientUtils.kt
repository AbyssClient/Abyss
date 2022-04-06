package de.vincentschweiger.phantomclient.utils.client

import de.vincentschweiger.phantomclient.Phantom
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger

// Easy access
val mc = MinecraftClient.getInstance()!!

val logger: Logger
    get() = Phantom.logger