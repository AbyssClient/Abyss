package me.cookie.abyssclient.cosmetics

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import me.cookie.abyssclient.Abyss.ROOT_URL
import me.cookie.abyssclient.config.ConfigSystem
import me.cookie.abyssclient.event.Listenable
import me.cookie.abyssclient.event.PlayerJoinWorldEvent
import me.cookie.abyssclient.event.handler
import me.cookie.abyssclient.server.getPlayer
import me.cookie.abyssclient.utils.client.mc
import me.cookie.abyssclient.utils.io.HttpClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.io.File

object Cosmetic : Listenable {

    private val cacheFolder = File(
        ConfigSystem.rootFolder,
        "cosmetics"
    ).apply {
        if (!exists()) {
            mkdirs()
        }
    }

    val capes = HashMap<String, Identifier>()

    val playerJoinHandler = handler<PlayerJoinWorldEvent> { event ->
        getPlayer(event.player.uuidAsString)
    }

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
        val obj = getPlayer(player.uuidAsString) ?: return null
        val short = obj.cosmetics.cape
        if (short.enabled) {
            if (capes.containsKey(short.name))
                return capes[short.name]
        }
        return null
    }
}