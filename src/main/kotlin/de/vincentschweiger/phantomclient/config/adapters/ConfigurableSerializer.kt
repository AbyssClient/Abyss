package de.vincentschweiger.phantomclient.config.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import de.vincentschweiger.phantomclient.config.Configurable
import java.lang.reflect.Type

object ConfigurableSerializer : JsonSerializer<Configurable> {

    override fun serialize(
            src: Configurable,
            typeOfSrc: Type,
            context: JsonSerializationContext
    ): JsonElement {
        val obj = JsonObject()

        obj.addProperty("name", src.name)
        obj.add("value", context.serialize(src.value))

        return obj
    }

}