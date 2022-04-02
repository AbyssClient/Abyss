package de.vincentschweiger.phantomclient.socket

import com.google.gson.Gson
import net.minecraft.client.MinecraftClient
import org.apache.commons.lang3.EnumUtils
import phantom.models.EnumRanks
import phantom.models.UserCosmetics
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.nio.charset.StandardCharsets

/**
 * Mi. 22/12/2021 16:09
 *
 * @author cook1e
 */
object ServerConnection {
    private var running = true
    private const val host = "phantom.vincentschweiger.de"
    private const val port = 19623
    private var outputStream: DataOutputStream? = null
    private var inputStream: DataInputStream? = null
    private var socket: Socket? = null
    private val gson: Gson = Gson()

    fun connect() {
        try {
            socket = Socket(host, port)
            outputStream = DataOutputStream(socket!!.getOutputStream())
            inputStream = DataInputStream(BufferedInputStream(socket!!.getInputStream()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        try {
            connect()
            setup()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setup() {
        try {
            val meMsg = "ME " + MinecraftClient.getInstance().session.uuid.replace("-".toRegex(), "")
            outputStream!!.writeInt(meMsg.length)
            outputStream!!.write(meMsg.toByteArray(StandardCharsets.UTF_8))
            outputStream!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isPlayer(uuid: String): Boolean {
        try {
            val msg = "IS $uuid"
            outputStream!!.writeInt(msg.length)
            outputStream!!.write(msg.toByteArray(StandardCharsets.UTF_8))
            outputStream!!.flush()
            return inputStream!!.readBoolean()
        } catch (e: SocketException) {
            connect()
            setup()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun getStringRepresentation(list: ArrayList<Char?>): String {
        val builder = StringBuilder(list.size)
        for (ch in list) {
            builder.append(ch)
        }
        return builder.toString()
    }

    fun getCosmetics(uuid: String): UserCosmetics? {
        try {
            val msg = "GET $uuid"
            outputStream!!.writeInt(msg.length)
            outputStream!!.write(msg.toByteArray(StandardCharsets.UTF_8))
            outputStream!!.flush()
            val length = inputStream!!.readInt()
            val buff = ByteArray(length)
            inputStream!!.readFully(buff)
            val s = String(buff)
            return gson.fromJson(s, UserCosmetics::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getRank(uuid: String): EnumRanks? {
        try {
            val msg = "RANK $uuid"
            outputStream!!.writeInt(msg.length)
            outputStream!!.write(msg.toByteArray(StandardCharsets.UTF_8))
            outputStream!!.flush()
            val length = inputStream!!.readInt()
            val chars = CharArray(length)
            for (i in 0 until length) {
                chars[i] = inputStream!!.readChar()
            }
            val rank = String(chars)
            return EnumUtils.getEnum(EnumRanks::class.java, rank)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun close() {
        try {
            running = false
            inputStream!!.close()
            outputStream!!.close()
            socket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}