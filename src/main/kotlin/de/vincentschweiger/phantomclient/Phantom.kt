package de.vincentschweiger.phantomclient

import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsModel
import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsRenderer
import de.vincentschweiger.phantomclient.cosmetics.hat.HatModel
import de.vincentschweiger.phantomclient.cosmetics.hat.HatRenderer
import de.vincentschweiger.phantomclient.event.*
import de.vincentschweiger.phantomclient.listeneres.OverlayListener
import de.vincentschweiger.phantomclient.modules.Modules
import de.vincentschweiger.phantomclient.modules.PositioningScreen
import de.vincentschweiger.phantomclient.modules.impl.ModuleClock
import de.vincentschweiger.phantomclient.modules.impl.ModuleFPS
import de.vincentschweiger.phantomclient.server.ServerConnection
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object Phantom : Listenable {
    const val CLIENT_NAME = "Phantom"
    val configDir = File(CLIENT_NAME)
    val serverConnection: ServerConnection = ServerConnection()
    private val logger: Logger = LoggerFactory.getLogger(CLIENT_NAME);
    private val kb: KeyBinding = KeyBindingHelper.registerKeyBinding(net.minecraft.client.option.KeyBinding("phantom.keybinding.positioning", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Phantom"))

    val tickHandler = handler<GameTickEvent> {
        if (kb.wasPressed()) MinecraftClient.getInstance().setScreen(PositioningScreen())
    }

    val startHandler = handler<ClientStartEvent> {
        runCatching {
            logger.info("Launching")
            serverConnection.setup()
            EntityModelLayerRegistry.registerModelLayer(HatRenderer.LAYER) { HatModel.getTexturedModelData() }
            EntityModelLayerRegistry.registerModelLayer(DragonwingsRenderer.LAYER) { DragonwingsModel.getTexturedModelData() }
            Modules.registerModule(ModuleFPS())
            Modules.registerModule(ModuleClock())
            Modules.getRegisteredModules().forEach {
                it.load()
            }
            EventManager
            OverlayListener
        }
    }

    val shutdownHandler = handler<ClientShutdownEvent> {
        logger.info("Shutting down client...")
        Modules.getRegisteredModules().forEach {
            it.save()
        }
    }
}