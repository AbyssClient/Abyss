package me.cookie.abyssclient.module

import me.cookie.abyssclient.config.ChoiceConfigurable
import me.cookie.abyssclient.config.Configurable
import me.cookie.abyssclient.config.util.Exclude
import me.cookie.abyssclient.event.*
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.client.toLowerCamelCase
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import java.awt.Color

abstract class Module(
        name: String,
        state: Boolean = false,
        @Exclude
        val disableActivation: Boolean = false,
) : Listenable, Configurable(name) {

    open val translationBaseKey: String
        get() = "phantom.module.${name.toLowerCamelCase()}"

    var enabled by boolean("Enabled", state)
            .listen { new ->
                runCatching {
                    // Check if player is in-game
                    if (mc.player == null || mc.world == null) {
                        return@runCatching
                    }
                    // Call enable or disable function
                    if (new) {
                        enable()
                    } else {
                        disable()
                    }
                }.onSuccess {
                    // Save new module state when module activation is enabled
                    if (disableActivation) {
                        return@listen false
                    }
                    // Call out module event
                    EventManager.callEvent(ToggleModuleEvent(this, new))
                    // Call to choices
                    value.filterIsInstance<ChoiceConfigurable>()
                            .forEach { it.newState(new) }
                }.onFailure {
                    // Log error
                    logger.error("Module failed to ${if (new) "enable" else "disable"}!", it)
                    // In case of an error module should stay disabled
                    throw it
                }

                new
            }


    /**
     * Quick access
     */
    protected val mc: MinecraftClient
        get() = me.cookie.abyssclient.utils.client.mc
    protected val player: ClientPlayerEntity
        get() = mc.player!!
    protected val world: ClientWorld
        get() = mc.world!!
    protected val network: ClientPlayNetworkHandler
        get() = mc.networkHandler!!
    protected val interaction: ClientPlayerInteractionManager
        get() = mc.interactionManager!!

    /**
     * Called when module is turned on
     */
    open fun enable() {}

    /**
     * Called when module is turned off
     */
    open fun disable() {}

    /**
     * Called when the module is added to the module manager
     */
    open fun init() {}
}
