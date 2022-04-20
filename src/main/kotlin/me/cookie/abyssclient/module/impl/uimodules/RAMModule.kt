package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule

object RAMModule : UIModule("RAM", Category.MISC) {
    override fun getText(): String {
        return "RAM: ${getMemPercent()}%"
    }

    private fun getMemPercent(): Int {
        val max = Runtime.getRuntime().maxMemory()
        val free = Runtime.getRuntime().freeMemory()
        return (max / free).toInt()
    }
}