package de.vincentschweiger.phantomclient.config.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import de.vincentschweiger.phantomclient.config.ChoiceConfigurable
import java.lang.reflect.Type

object ChoiceConfigurableSerializer : JsonSerializer<ChoiceConfigurable> {

    override fun serialize(src: ChoiceConfigurable, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val obj = JsonObject()

        obj.addProperty("name", src.name)
        obj.addProperty("active", src.activeChoice.choiceName)
        obj.add("value", context.serialize(src.value))

        val choices = JsonObject()

        for (choice in src.choices) {
            choices.add(choice.name, context.serialize(choice))
        }

        obj.add("choices", choices)

        return obj
    }

}

