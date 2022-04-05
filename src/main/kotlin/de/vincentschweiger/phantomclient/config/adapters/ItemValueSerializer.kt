package de.vincentschweiger.phantomclient.config.adapters

import com.google.gson.*
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.reflect.Type

object ItemValueSerializer : JsonSerializer<Item>, JsonDeserializer<Item> {

    override fun serialize(src: Item, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(Registry.ITEM.getId(src).toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Item {
        return Registry.ITEM.get(Identifier.tryParse(json.asString))
    }

}