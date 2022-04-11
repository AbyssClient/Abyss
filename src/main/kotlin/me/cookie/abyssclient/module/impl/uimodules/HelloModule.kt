package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.UIModule

object HelloModule : UIModule("hello") {
    override fun getText(): String {
        return "Hello!"
    }
}