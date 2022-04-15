package me.cookie.abyssclient.server

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.luben.zstd.Zstd
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import me.cookie.abyssclient.utils.client.logger
import me.cookie.abyssclient.utils.client.mc
import java.util.*
import java.util.concurrent.TimeUnit

val playerCache: Cache<String, Player> = Caffeine.newBuilder().maximumSize(1_000).expireAfterWrite(
    25, TimeUnit.MINUTES
).recordStats().build()

private val queue = LinkedList<String>()
fun getPlayer(uuid: String): Player? {
    if (playerCache.getIfPresent(uuid) != null) return playerCache.getIfPresent(uuid)!!
    if (!queue.contains(uuid)) queue.add(uuid)

    return null
}

object SocketClient {
    val thread = GlobalScope.launch {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }
        client.wss(host = "abyss.vincentschweiger.de", port = 443, path = "/ws") {
            // Shake hands
            val str = "handshake\r${mc.session.uuid}\r"
            sendCompressed(str)
            getPlayer("77798ea5-bc8b-4d46-8eb7-22d56ee142b1")
            launch {
                while (isActive) while (!queue.isEmpty()) sendCompressed("player\r${queue.poll()}\r")
            }
            while (isActive) {
                incoming.consumeEach { frame ->
                    val packet = readCompressed(frame.readBytes())
                    val split = packet.split('\r')
                    if (split.size < 0) return@consumeEach
                    when (split[0]) {
                        "player" -> {
                            val uuid = split[1]
                            val state = split[2].toBooleanStrict()
                            if (state) {
                                val cape = split[3]
                                playerCache.put(uuid, Player(uuid, state, Cosmetics(Cape(cape))))
                            } else playerCache.put(uuid, Player(uuid, state))
                        }
                    }
                }
            }
        }
    }
}


/**
 * Sends a zstd compressed message to other
 */
suspend fun WebSocketSession.sendCompressed(msg: String) {
    send(Zstd.compress(msg.encodeToByteArray()))
}

/**
 * Read a compressed string from other
 */
suspend fun readCompressed(buf: ByteArray): String {
    val receivedBytes = ByteArray(512)
    Zstd.decompress(receivedBytes, buf)
    return receivedBytes.decodeToString()
}