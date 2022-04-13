package me.cookie.abyssclient.server

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.luben.zstd.Zstd
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import me.cookie.abyssclient.utils.client.mc
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch
import me.cookie.abyssclient.utils.client.logger

private val playerCache: Cache<String, Player> = Caffeine.newBuilder().maximumSize(1_000).expireAfterWrite(
    60, TimeUnit.MINUTES
).recordStats().build()

private val queue = LinkedList<String>()
fun getPlayer(uuid: String): Player? {
    if (playerCache.getIfPresent(uuid) != null) {
        println("Get from cache")
        return playerCache.getIfPresent(uuid)!!
    }
    if (!queue.contains(uuid)) {
        println("Adding to queue")
        queue.add(uuid)
    }
    return null
}

object SocketClient {
    val job = GlobalScope.launch {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }
        while (mc.player == null) {
            delay(2000)
        }
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/ws") {
            // Shake hands
            val str = "handshake\r${mc.player!!.gameProfile!!.id}\r"
            sendCompressed(str)
            // Handle incoming packets
            launch {
                while (isActive) {
                    // Get data from server (if they sent any)
                    val compReceivedBytes = incoming.receive() as? Frame.Binary
                    if (compReceivedBytes != null) {
                        val msg = readCompressed(compReceivedBytes.readBytes())
                        val list = msg.split("\r")
                        if (list[0] == "player") {
                            val uuid = list[1]
                            val state = list[2].toBooleanStrict()
                            queue.remove(uuid)
                            playerCache.put(uuid, Player(uuid, state))
                        }
                    }
                }
            }
            launch {
                while (isActive) {
                    // Check for stuff in queue
                    if (!queue.isEmpty()) {
                        while (!queue.isEmpty()) {
                            //logger.info("Requesting player ${queue.first}")
                            sendCompressed("player\r${queue.poll()}\r")
                        }
                    }
                }
            }
            while (isActive) {
                delay(500)
            }
            client.close()
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
fun readCompressed(buf: ByteArray): String {
    val receivedBytes = ByteArray(512)
    Zstd.decompress(receivedBytes, buf)
    return receivedBytes.decodeToString()
}