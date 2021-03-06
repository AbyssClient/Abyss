package me.cookie.abyssclient.module.impl.uimodules

import me.cookie.abyssclient.module.Category
import me.cookie.abyssclient.module.UIModule
import java.text.SimpleDateFormat
import java.util.*

object ClockModule : UIModule("clock", Category.MISC) {
    private val formatter = SimpleDateFormat("[HH:mm]")
    override fun getText(): String {
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
}