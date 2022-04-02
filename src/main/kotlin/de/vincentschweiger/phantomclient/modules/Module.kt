package de.vincentschweiger.phantomclient.modules

import de.vincentschweiger.phantomclient.event.Listenable
import java.math.BigDecimal
import java.math.RoundingMode

abstract class Module : Listenable {
    var isEnabled = true
        private set

    abstract val name: String

    fun switchEnabled() {
        isEnabled = !isEnabled
    }

    fun save() {
    }

    open fun load() {
    }

    companion object {
        fun round(value: Double, places: Int): Double {
            require(places >= 0)
            var bd = BigDecimal.valueOf(value)
            bd = bd.setScale(places, RoundingMode.HALF_UP)
            return bd.toDouble()
        }
    }
}