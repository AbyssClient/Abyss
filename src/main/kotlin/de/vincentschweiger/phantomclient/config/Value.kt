package de.vincentschweiger.phantomclient.config

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.mojang.brigadier.StringReader
import de.vincentschweiger.phantomclient.config.util.Exclude
import de.vincentschweiger.phantomclient.event.EventManager
import de.vincentschweiger.phantomclient.event.ValueChangedEvent
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*
import kotlin.reflect.KProperty

typealias ValueListener<T> = (T) -> T

/**
 * Value based on generics and support for readable names and description
 */
open class Value<T : Any>(
        @SerializedName("name") open val name: String,
        @SerializedName("value") internal var value: T,
        @Exclude val valueType: ValueType,
) {

    @Exclude
    private val listeners = mutableListOf<ValueListener<T>>()

    /**
     * Support for delegated properties
     * example:
     *  var autoaim by boolean(name = "autoaim", default = true)
     *  if(!autoaim)
     *    autoaim = true
     *
     * Important: To use values a class has to be configurable
     *
     * @docs https://kotlinlang.org/docs/reference/delegated-properties.html
     */

    operator fun getValue(u: Any?, property: KProperty<*>) = get()

    operator fun setValue(u: Any?, property: KProperty<*>, t: T) {
        set(t)
    }

    fun get() = value

    fun set(t: T) { // temporary set value
        value = t

        // check if value is really accepted
        var currT = t
        runCatching {
            listeners.forEach {
                currT = it(t)
            }
        }.onSuccess {
            value = currT
            EventManager.callEvent(ValueChangedEvent(this))
        }
    }

    fun type() = valueType

    fun listen(listener: ValueListener<T>): Value<T> {
        listeners += listener
        return this
    }

    /**
     * Deserialize value from JSON
     */
    open fun deserializeFrom(gson: Gson, element: JsonElement) {
        val currValue = this.value
        set(
                gson.fromJson(element, currValue.javaClass)
        )
    }

    open fun setByString(string: String) {
        if (this.value is Boolean) {
            val newValue = when (string.lowercase(Locale.ROOT)) {
                "true", "on" -> true
                "false", "off" -> false
                else -> throw IllegalArgumentException()
            }
            set(newValue as T)
        } else if (this.value is Block) {
            set(Registry.BLOCK.get(Identifier.fromCommandInput(StringReader(string))) as T)
        } else if (this.value is Item) {
            set(Registry.ITEM.get(Identifier.fromCommandInput(StringReader(string))) as T)
        } else {
            throw IllegalStateException()
        }
    }

}

/**
 * Ranged value adds support for closed ranges
 */
class RangedValue<T : Any>(
        name: String,
        value: T,
        @Exclude val range: ClosedRange<*>,
        type: ValueType
) : Value<T>(name, value, valueType = type) {

    fun getFrom(): Double {
        return (this.range.start as Number).toDouble()
    }

    fun getTo(): Double {
        return (this.range.endInclusive as Number).toDouble()
    }

    override fun setByString(string: String) {
        if (this.value is ClosedRange<*>) {
            val split = string.split("..")

            if (split.size != 2) throw IllegalArgumentException()

            val closedRange = this.value as ClosedRange<*>

            val newValue = when (closedRange.start) {
                is Int -> split[0].toInt()..split[1].toInt()
                is Long -> split[0].toLong()..split[1].toLong()
                is Float -> split[0].toFloat()..split[1].toFloat()
                is Double -> split[0].toDouble()..split[1].toDouble()
                else -> throw IllegalStateException()
            }

            set(newValue as T)
        } else {
            val translationFunction: (String) -> Any = when (this.value) {
                is Int -> String::toInt
                is Long -> String::toLong
                is Float -> String::toFloat
                is Double -> String::toDouble
                else -> throw IllegalStateException()
            }

            set(translationFunction(string) as T)
        }
    }

}

class ChooseListValue<T : NamedChoice>(
        name: String,
        value: T,
        @Exclude val choices: Array<T>
) : Value<T>(name, value, ValueType.CHOOSE) {

    override fun deserializeFrom(gson: Gson, element: JsonElement) {
        val name = element.asString

        setFromValueName(name)
    }

    fun setFromValueName(name: String?) {
        this.value = choices.first { it.choiceName == name }
    }

    fun getChoicesStrings(): Array<String> {
        return this.choices.map { it.choiceName }.toTypedArray()
    }

    override fun setByString(string: String) {
        set(this.choices.firstOrNull { it.choiceName.equals(string, true) }!! as T)
    }
}

interface NamedChoice {
    val choiceName: String
}

enum class ValueType {
    BOOLEAN, FLOAT, FLOAT_RANGE, INT, INT_RANGE, TEXT, TEXT_ARRAY, CURVE, COLOR, BLOCK, BLOCKS, ITEM,
    ITEMS, CHOICE, CHOOSE, INVALID, CONFIGURABLE, TOGGLEABLE, DOUBLE
}