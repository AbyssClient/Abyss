package me.cookie.abyssclient.cosmetics

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import me.cookie.abyssclient.Abyss.ROOT_URL
import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.config.Configurable
import me.cookie.abyssclient.event.Listenable
import me.cookie.abyssclient.server.getPlayer
import me.cookie.abyssclient.utils.client.mc
import me.cookie.abyssclient.utils.io.HttpClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.io.File

object Cosmetic : Listenable {

    object Options : Configurable("cosmetics") {
        // Should render capes?
        val capes by boolean("capes", true)
    }

    init {
        ConfigSystem.root(Options)
    }

    private val cacheFolder = File(
        ConfigSystem.rootFolder,
        "cosmetics"
    ).apply {
        if (!exists()) {
            mkdirs()
        }
    }

    val capes = HashMap<String, Identifier>()

    fun loadCapes() {
        runBlocking {
            val capesJson = HttpClient.get("$ROOT_URL/api/capes")
            val avail = JsonParser.parseString(capesJson).asJsonArray
            for (cape in avail) {
                val obj = cape.asJsonObject
                val name = obj.get("name").asString
                val path = obj.get("path").asString
                val dest = File(cacheFolder, "$name.png")
                if (!dest.exists()) HttpClient.download("$ROOT_URL$path", dest)
                val textureManager = mc.textureManager
                val nativeImage = NativeImage.read(dest.inputStream())
                val identifier = textureManager.registerDynamicTexture("abyss", NativeImageBackedTexture(nativeImage))
                capes[name] = identifier
            }
        }
    }

    fun getCapeTexture(player: AbstractClientPlayerEntity): Identifier? {
        if (!Options.capes) return null
        val obj = getPlayer(player.uuidAsString) ?: return null
        val short = obj.cosmetics.cape
        if (short.enabled) {
            if (capes.containsKey(short.name))
                return capes[short.name]
        }
        return null
    }
}