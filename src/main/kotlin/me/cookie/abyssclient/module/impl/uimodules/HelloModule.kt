package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule

object HelloModule : UIModule("hello", Category.MISC) {
    override fun getText(): String {
        return "Hello!"
    }
}