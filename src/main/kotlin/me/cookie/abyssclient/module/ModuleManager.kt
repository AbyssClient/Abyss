package me.cookie.abyssclient.module

import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.event.Listenable
import me.cookie.abyssclient.module.impl.ClockModule
import me.cookie.abyssclient.module.impl.FpsModule
import me.cookie.abyssclient.module.impl.HelloModule

private val modules = mutableListOf<Module>()

object ModuleManager : Listenable, Iterable<Module> by modules {

    init {
        ConfigSystem.root("modules", modules)
    }

    /**
     * Register inbuilt client modules
     */
    fun registerInbuilt() {
        val builtin = arrayOf(
            FpsModule,
            ClockModule,
            HelloModule
        )
        builtin.apply {
            sortBy { it.name }
            forEach(::addModule)
        }
    }

    /**
     * Allow `ModuleManager += Module` syntax
     */
    operator fun plusAssign(module: Module) {
        addModule(module)
    }

    /**
     * Initialize and add a module
     *
     * @param module The Module to add
     */
    fun addModule(module: Module) {
        module.initConfigurable()
        module.init()
        modules += module
    }

    /**
     * @return A list of UIModules (Modules with UI)
     */
    fun getUIModules(): List<UIModule> {
        val uimodules = mutableListOf<UIModule>()
        modules.forEach {
            if (it is UIModule) uimodules.add(it)
        }
        return uimodules
    }

    /**
     * ?Autocomplete module names ig?
     * stolen from liquidbounce
     */
    fun autoComplete(begin: String, validator: (Module) -> Boolean = { true }): List<String> {
        return filter { it.name.startsWith(begin, true) && validator(it) }.map { it.name }
    }
}