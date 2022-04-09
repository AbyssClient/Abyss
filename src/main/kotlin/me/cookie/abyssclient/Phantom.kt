package me.cookie.abyssclient

import me.cookie.abyssclient.command.CommandManager
import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.event.*
import me.cookie.abyssclient.module.ModuleManager
import me.cookie.abyssclient.render.ultralight.UltralightEngine
import me.cookie.abyssclient.ui.PositioningScreen
import me.cookie.abyssclient.ui.ConfigScreen
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

object Phantom : Listenable {
    const val CLIENT_NAME = "Phantom"
    val CLIENT_VERSION: String = FabricLoader.getInstance().getModContainer("phantom").get().metadata.version.friendlyString
    const val CLIENT_AUTHOR = "Vento"
    const val LIQUID_CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    val logger: Logger = LoggerFactory.getLogger(me.cookie.abyssclient.Phantom.CLIENT_NAME)!!
    private val kb: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("phantom.keybinding.positioning", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Phantom"))
    private val kb2: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding("other config screen thing", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "Phantom"))

    /**
     * Called every tick
     */
    val tickHandler = handler<GameTickEvent> {
        if (me.cookie.abyssclient.Phantom.kb.wasPressed()) MinecraftClient.getInstance().setScreen(PositioningScreen())
        if (me.cookie.abyssclient.Phantom.kb2.wasPressed()) MinecraftClient.getInstance().setScreen(ConfigScreen())
    }

    /**
     * Called on startup
     */
    val startHandler = handler<ClientStartEvent> {
        runCatching {
            me.cookie.abyssclient.Phantom.logger.info("Launching ${me.cookie.abyssclient.Phantom.CLIENT_NAME} v${me.cookie.abyssclient.Phantom.CLIENT_VERSION} by ${me.cookie.abyssclient.Phantom.CLIENT_AUTHOR}")
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
        }.onSuccess {
            me.cookie.abyssclient.Phantom.logger.info("Successfully loaded client!")
        }.onFailure {
            me.cookie.abyssclient.Phantom.logger.error("Unable to load client.", it)
            exitProcess(1)
        }
    }

    /**
     * Called on shutdown
     */
    val shutdownHandler = handler<ClientShutdownEvent> {
        me.cookie.abyssclient.Phantom.logger.info("Shutting down phantom ...")
        ConfigSystem.store()
        UltralightEngine.shutdown()
    }
}