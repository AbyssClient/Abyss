package me.cookie.abyssclient.module.impl

import me.cookie.abyssclient.module.UIModule
import java.text.SimpleDateFormat
import java.util.*

object ClockModule : UIModule("clock") {
    private val formatter = SimpleDateFormat("[HH:mm]")
    override fun getText(): String {
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
}