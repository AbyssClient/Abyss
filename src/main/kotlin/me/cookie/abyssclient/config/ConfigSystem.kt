package me.cookie.abyssclient.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import me.cookie.abyssclient.config.adapters.ChoiceConfigurableSerializer
import me.cookie.abyssclient.config.adapters.ConfigurableSerializer
import me.cookie.abyssclient.config.adapters.ItemValueSerializer
import me.cookie.abyssclient.config.util.ExcludeStrategy
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.client.mc
import net.minecraft.item.Item
import java.io.File

object ConfigSystem {
    val rootFolder = File(
        mc.runDirectory,
        me.cookie.abyssclient.Abyss.CLIENT_NAME
    ).apply { // Check if there is already a config folder and if not create new folder (mkdirs not needed - .minecraft should always exist)
        if (!exists()) {
            mkdir()
        }
    }

    private val configurables: MutableList<Configurable> = mutableListOf()
    private val confType = TypeToken.get(Configurable::class.java).type
    private val gson = GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(ExcludeStrategy(false))
        .registerTypeHierarchyAdapter(Item::class.javaObjectType, ItemValueSerializer)
        .registerTypeAdapter(ChoiceConfigurable::class.javaObjectType, ChoiceConfigurableSerializer)
        .registerTypeHierarchyAdapter(Configurable::class.javaObjectType, ConfigurableSerializer)
        .create()

    /**
     * Create a new root configurable
     */
    fun root(name: String, tree: MutableList<out Configurable> = mutableListOf()) {
        @Suppress("UNCHECKED_CAST") root(Configurable(name, tree as MutableList<Value<*>>))
    }

    /**
     * Add a root configurable
     */
    fun root(configurable: Configurable) {
        configurable.initConfigurable()
        configurables.add(configurable)
    }

    /**
     * All configurables should load now.
     */
    fun load() {
        for (configurable in configurables) { // Make a new .json file to save our root configurable
            File(rootFolder, "${configurable.name.lowercase()}.json").runCatching {
                if (!exists()) {
                    store()
                    return@runCatching
                }
                logger.debug("Reading config ${configurable.name}...")
                JsonParser.parseReader(gson.newJsonReader(reader()))?.let { deserializeConfigurable(configurable, it) }
                logger.info("Successfully loaded config '${configurable.name}'.")
            }.onFailure {
                logger.error("Unable to load config ${configurable.name}", it)
                store()
            }
        }
    }


    private fun deserializeConfigurable(configurable: Configurable, jsonElement: JsonElement) {
        runCatching {
            val jsonObject = jsonElement.asJsonObject

            if (jsonObject.getAsJsonPrimitive("name").asString != configurable.name) {
                throw IllegalStateException()
            }

            val values =
                jsonObject.getAsJsonArray("value").map { it.asJsonObject }.associateBy { it["name"].asString!! }

            for (value in configurable.value) {
                if (value is Configurable) {
                    val currentElement = values[value.name] ?: continue

                    runCatching {
                        if (value is ChoiceConfigurable) {
                            runCatching {
                                val newActive = currentElement["active"].asString

                                value.setFromValueName(newActive)
                            }.onFailure { it.printStackTrace() }

                            val choices = currentElement["choices"].asJsonObject

                            for (choice in value.choices) {
                                runCatching {
                                    val choiceElement = choices[choice.name]

                                    deserializeConfigurable(choice, choiceElement)
                                }.onFailure { it.printStackTrace() }
                            }
                        }
                    }.onFailure { it.printStackTrace() }

                    deserializeConfigurable(value, currentElement)
                } else {
                    val currentElement = values[value.name] ?: continue

                    runCatching {
                        value.deserializeFrom(gson, currentElement["value"])
                    }.onFailure { it.printStackTrace() }
                }

            }
        }.onFailure { it.printStackTrace() }
    }

    /**
     * All configurables should store now.
     */
    fun store() {
        for (configurable in configurables) { // Make a new .json file to save our root configurable
            File(rootFolder, "${configurable.name.lowercase()}.json").runCatching {
                if (!exists()) {
                    createNewFile().let { logger.debug("Created new file (status: $it)") }
                }
                logger.debug("Writing config ${configurable.name}...")
                gson.newJsonWriter(writer()).use {
                    gson.toJson(configurable, confType, it)
                }
                logger.info("Successfully saved config '${configurable.name}'.")
            }.onFailure {
                logger.error("Unable to store config ${configurable.name}", it)
            }
        }
    }

}