package me.cookie.abyssclient.command.builder

import me.cookie.abyssclient.command.AutoCompletionHandler
import me.cookie.abyssclient.command.Parameter
import me.cookie.abyssclient.command.ParameterValidationResult
import me.cookie.abyssclient.command.ParameterVerifier
import me.cookie.abyssclient.module.ModuleManager
import me.cookie.abyssclient.module.Module

class ParameterBuilder<T> private constructor(val name: String) {

    private var verifier: ParameterVerifier<T>? = null
    private var required: Boolean? = null
    private var vararg: Boolean = false
    private var autocompletionHandler: AutoCompletionHandler? = null
    private var useMinecraftAutoCompletion: Boolean = false

    companion object {
        val STRING_VALIDATOR: ParameterVerifier<String> = { ParameterValidationResult.ok(it) }
        val MODULE_VALIDATOR: ParameterVerifier<Module> = { name ->
            val mod = ModuleManager.find { it.name.equals(name, true) }

            if (mod == null) {
                ParameterValidationResult.error("Module '$name' not found")
            } else {
                ParameterValidationResult.ok(mod)
            }
        }
        val INTEGER_VALIDATOR: ParameterVerifier<Int> = {
            try {
                ParameterValidationResult.ok(it.toInt())
            } catch (e: NumberFormatException) {
                ParameterValidationResult.error("'$it' is not a valid integer")
            }
        }
        val POSITIVE_INTEGER_VALIDATOR: ParameterVerifier<Int> = {
            try {
                val integer = it.toInt()

                if (integer >= 0) {
                    ParameterValidationResult.ok(integer)
                } else {
                    ParameterValidationResult.error("The integer must be positive")
                }
            } catch (e: NumberFormatException) {
                ParameterValidationResult.error("'$it' is not a valid integer")
            }
        }

        fun <T> begin(name: String): ParameterBuilder<T> = ParameterBuilder(name)

        fun autocompleteWithList(supplier: () -> Iterable<String>): AutoCompletionHandler =
                { start -> supplier().filter { it.startsWith(start, true) } }
    }

    fun verifiedBy(verifier: ParameterVerifier<T>): ParameterBuilder<T> {
        this.verifier = verifier

        return this
    }

    fun optional(): ParameterBuilder<T> {
        this.required = false

        return this
    }

    /**
     * Marks this parameter as a vararg.
     *
     * The values are stored in an array
     *
     * Only allowed at the end.
     */
    fun vararg(): ParameterBuilder<T> {
        this.vararg = true

        return this
    }

    fun required(): ParameterBuilder<T> {
        this.required = true

        return this
    }

    fun autocompletedWith(autocompletionHandler: AutoCompletionHandler): ParameterBuilder<T> {
        this.autocompletionHandler = autocompletionHandler

        return this
    }

    fun useMinecraftAutoCompletion(): ParameterBuilder<T> {
        this.useMinecraftAutoCompletion = true

        return this
    }

    fun build(): Parameter<T> {
        if (this.useMinecraftAutoCompletion && autocompletionHandler != null) {
            throw IllegalArgumentException("Standard Minecraft autocompletion was enabled and an autocompletion handler was set")
        }

        return Parameter(
                this.name,
                this.required
                        ?: throw IllegalArgumentException("The parameter was neither marked as required nor as optional."),
                this.vararg,
                this.verifier,
                autocompletionHandler,
                useMinecraftAutoCompletion
        )
    }

}