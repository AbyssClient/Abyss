package de.vincentschweiger.phantomclient.module.impl

import de.vincentschweiger.phantomclient.module.UIModule

object HelloModule : UIModule("hello") {
    override fun getText(): String {
        return "Hello!"
    }
}