package de.vincentschweiger.phantomclient.modules.impl

import de.vincentschweiger.phantomclient.modules.UIModule
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

class ModuleClock : UIModule() {
    override val name: String
        get() = "Clock"
    override val text: String
        get() {
            val formatter = SimpleDateFormat("[HH:mm]")
            val date = Date(System.currentTimeMillis())
            return formatter.format(date)
        }
    override val color: Color
        get() = Color(255, 100, 0)
}
