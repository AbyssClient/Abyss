package de.vincentschweiger.phantomclient.module.impl

import de.vincentschweiger.phantomclient.module.UIModule
import java.text.SimpleDateFormat
import java.util.Date

object ClockModule : UIModule("clock") {
    private val formatter = SimpleDateFormat("[HH:mm]")
    override fun getText(): String {
        val date = Date(System.currentTimeMillis())
        return formatter.format(date)
    }
}