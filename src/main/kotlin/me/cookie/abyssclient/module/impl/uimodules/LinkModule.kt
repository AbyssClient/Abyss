package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule
import java.awt.Color

object LinkModule : UIModule("LINK", Category.MISC){
    override fun getText(): String {
        return "Link ist cool"
    }

    override val color: Color
        get() = Color.GREEN
}