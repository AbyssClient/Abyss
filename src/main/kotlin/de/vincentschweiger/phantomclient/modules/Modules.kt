package de.vincentschweiger.phantomclient.modules

import java.util.*


object Modules {
    private val registeredModules: MutableList<UIModule> = ArrayList()

    fun registerModule(module: UIModule) {
        registeredModules.add(module)
    }

    fun getRegisteredModules(): List<UIModule> {
        return registeredModules
    }
}