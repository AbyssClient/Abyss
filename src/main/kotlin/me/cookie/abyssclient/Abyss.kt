package me.cookie.abyssclient

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.cosmetics.Cosmetic
import me.cookie.abyssclient.event.*
import me.cookie.abyssclient.module.ModuleManager
import me.cookie.abyssclient.server.SocketClient
import me.cookie.abyssclient.ui.ModuleScreen
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
    const val ROOT_URL = "https://abyss.vincentschweiger.de"

    val logger: Logger = LoggerFactory.getLogger(CLIENT_NAME)!!
    private val pkb: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            "abyss.keybinding.positioning",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "Phantom"
        )
    )

    private val mkb: KeyBinding = KeyBindingHelper.registerKeyBinding(
        KeyBinding(
            "abyss.keybinding.modules",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "Phantom"
        )
    )

    /**
     * Called every tick
     */
    val tickHandler = handler<GameTickEvent> {
        if (pkb.wasPressed())
            MinecraftClient.getInstance().setScreen(PositioningScreen())
        else if (mkb.wasPressed())
            MinecraftClient.getInstance().setScreen(ModuleScreen())
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
            // Register commands & modules
            ModuleManager.registerInbuilt()
            CommandManager.registerInbuilt()
            // Load config
            ConfigSystem.load()
            SocketClient.job.start()
            Cosmetic
            Cosmetic.loadCapes()
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
        runBlocking {
            logger.info("Shutting down abyss ...")
            ConfigSystem.store()
            SocketClient.job.cancelAndJoin()
            logger.info("Shut down abyss")
        }
    }
}