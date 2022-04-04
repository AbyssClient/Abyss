package de.vincentschweiger.phantomclient

import de.vincentschweiger.phantomclient.config.DefaultConfig
import de.vincentschweiger.phantomclient.cosmetics.CosmeticManager
import de.vincentschweiger.phantomclient.event.*
import de.vincentschweiger.phantomclient.module.ModuleManager
import de.vincentschweiger.phantomclient.module.PositioningScreen
import de.vincentschweiger.phantomclient.socket.ServerConnection
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object Phantom : Listenable {
    const val CLIENT_NAME = "Phantom"
    val CLIENT_VERSION: String = FabricLoader.getInstance().getModContainer("phantom").get().metadata.version.friendlyString
    const val CLIENT_AUTHOR = "Vento"

    val config = DefaultConfig(File("$CLIENT_NAME.json"))

    val serverConnection = ServerConnection
    val logger: Logger = LoggerFactory.getLogger(CLIENT_NAME)!!
    private val kb: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("phantom.keybinding.positioning", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Phantom"))

    /**
     * Called every tick
     */
    val tickHandler = handler<GameTickEvent> {
        if (kb.wasPressed()) MinecraftClient.getInstance().setScreen(PositioningScreen())
    }

    /**
     * Called on startup
     */
    val startHandler = handler<ClientStartEvent> {
        runCatching {
            logger.info("Launching $CLIENT_NAME v$CLIENT_VERSION by $CLIENT_AUTHOR")
            serverConnection.setup()
            // Stuff
            EventManager
            ModuleManager
            CosmeticManager
            // Register modules
            ModuleManager.registerInbuilt()
        }
    }

    /**
     * Called on shutdown
     */
    val shutdownHandler = handler<ClientShutdownEvent> {
        logger.info("Shutting down phantom ...")
        config.save()
        serverConnection.close()
    }
}