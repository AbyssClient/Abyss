package de.vincentschweiger.phantomclient.config

import de.vincentschweiger.phantomclient.config.util.Exclude
import de.vincentschweiger.phantomclient.event.Listenable
import de.vincentschweiger.phantomclient.utils.client.toLowerCamelCase
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.TranslatableText
import de.vincentschweiger.phantomclient.module.Module

/**
 * Should handle events when enabled. Allows the client-user to toggle features. (like modules)
 */
open class ToggleableConfigurable(@Exclude val module: Module? = null, name: String, enabled: Boolean) : Listenable,
        Configurable(name, valueType = ValueType.TOGGLEABLE) {

    val translationBaseKey: String
        get() = "${module?.translationBaseKey}.value.${name.toLowerCamelCase()}"

    val description: TranslatableText
        get() = TranslatableText("$translationBaseKey.description")

    var enabled by boolean("Enabled", enabled)

    override fun handleEvents() = super.handleEvents() && enabled

    override fun parent() = module

    fun getEnabledValue(): Value<*> {
        return this.value[0]
    }
}

/**
 * Allows to configure and manage modes
 */
open class ChoiceConfigurable(
        @Exclude val module: Module,
        name: String,
        var activeChoice: Choice,
        choicesCallback: (ChoiceConfigurable) -> Array<Choice>
) : Configurable(name, valueType = ValueType.CHOICE) {

    val choices: Array<Choice>
    val translationBaseKey: String
        get() = "${module.translationBaseKey}.value.${name.toLowerCamelCase()}"

    val description: TranslatableText
        get() = TranslatableText("$translationBaseKey.description")

    init {
        this.choices = choicesCallback(this)
    }

    fun newState(state: Boolean) {
        if (state) {
            this.activeChoice.enable()
        } else {
            this.activeChoice.disable()
        }
    }

    fun setFromValueName(name: String) {
        this.activeChoice = choices.first { it.choiceName == name }
    }
}

/**
 * A mode is sub-module to separate different bypasses into extra classes
 */
abstract class Choice(name: String) : Configurable(name), Listenable, NamedChoice {

    private val translationBaseKey: String
        get() = "${this.parent.translationBaseKey}.choice.${name.toLowerCamelCase()}"

    val description: TranslatableText
        get() = TranslatableText("$translationBaseKey.description")

    override val choiceName: String
        get() = this.name

    /**
     * Quick access
     */
    protected val mc: MinecraftClient
        get() = de.vincentschweiger.phantomclient.utils.client.mc
    protected val player: ClientPlayerEntity
        get() = mc.player!!
    protected val world: ClientWorld
        get() = mc.world!!
    protected val network: ClientPlayNetworkHandler
        get() = mc.networkHandler!!

    val isActive: Boolean
        get() = this.parent.activeChoice === this

    abstract val parent: ChoiceConfigurable

    /**
     * Called when module is turned on
     */
    open fun enable() {}

    /**
     * Called when module is turned off
     */
    open fun disable() {}

    /**
     * Events should be handled when mode is enabled
     */
    override fun handleEvents() = super.handleEvents() && isActive

    /**
     * Parent listenable
     */
    override fun parent() = this.parent.module

}

/**
 * Empty mode. It does nothing. Use it when you want a client-user to disable a feature.
 */
class NoneChoice(override val parent: ChoiceConfigurable) : Choice("None")