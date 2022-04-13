package me.cookie.abyssclient

import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.cosmetics.Cosmetic
import me.cookie.abyssclient.event.*
import me.cookie.abyssclient.module.ModuleManager
import me.cookie.abyssclient.render.ultralight.UltralightEngine
import me.cookie.abyssclient.server.SocketClient
import me.cookie.abyssclient.ui.PositioningScreen
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object Abyss : Listenable {
    const val CLIENT_NAME = "Abyss"
    val CLIENT_VERSION: String =
        FabricLoader.getInstance().getModContainer("abyss").get().metadata.version.friendlyString
    const val CLIENT_AUTHOR = "Vento"
    const val LIQUID_CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    val logger: Logger = LoggerFactory.getLogger(CLIENT_NAME)!!
    private val kb: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            "abyss.keybinding.positioning",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "Phantom"
        )
    )

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
            // Stuff
            EventManager
            ConfigSystem
            ModuleManager
            CommandManager
            UltralightEngine.init()
            // Register commands & modules
            ModuleManager.registerInbuilt()
            CommandManager.registerInbuilt()
            // Load config
            ConfigSystem.load()
            SocketClient.job.start()
            Cosmetic
        }.onSuccess {
            logger.info("Successfully loaded client!")
        }.onFailure {
            logger.error("Unable to load client.", it)
            exitProcess(1)
        }
    }

    /**
     * Called on shutdown
     */
    val shutdownHandler = handler<ClientShutdownEvent> {
        logger.info("Shutting down abyss ...")
        ConfigSystem.store()
        UltralightEngine.shutdown()
        SocketClient.job.cancelChildren()
        SocketClient.job.cancel()
    }
}