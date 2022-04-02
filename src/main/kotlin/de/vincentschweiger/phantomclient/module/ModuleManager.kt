package de.vincentschweiger.phantomclient.module

import de.vincentschweiger.phantomclient.event.Listenable
import de.vincentschweiger.phantomclient.module.impl.FpsModule

private val modules = mutableListOf<Module>()

object ModuleManager : Listenable, Iterable<de.vincentschweiger.phantomclient.module.Module> by modules {

    /**
     * Register inbuilt client modules
     */
    fun registerInbuilt() {
        val builtin = arrayOf(
                FpsModule
        )
        builtin.apply {
            sortBy { it.getName() }
            forEach(::addModule)
        }
    }

    /**
     * Allow `ModuleManager += Module` syntax
     */
    operator fun plusAssign(module: Module) {
        addModule(module)
    }

    fun addModule(module: Module) {
        //module.initConfigurable()
        //module.init()
        modules += module
    }

    fun getUIModules(): List<UIModule> {
        val uimodules = mutableListOf<UIModule>()
        modules.forEach {
            if (it is UIModule) uimodules.add(it)
        }
        return uimodules
    }

    fun autoComplete(begin: String, validator: (Module) -> Boolean = { true }): List<String> {
        return filter { it.getName().startsWith(begin, true) && validator(it) }.map { it.getName() }
    }
}