package me.cookie.abyssclient.config.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.cookie.abyssclient.config.Value
import java.lang.reflect.Type

object ValueSerializationAdapter : JsonSerializer<Value<*>> {

    override fun serialize(src: Value<*>, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()

        obj.addProperty("name", src.name)
        obj.add("value", context.serialize(src.value))

        return obj
    }

}