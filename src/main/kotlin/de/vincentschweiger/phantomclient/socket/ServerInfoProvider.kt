package de.vincentschweiger.phantomclient.socket

import de.vincentschweiger.phantomclient.Phantom.serverConnection
import phantom.models.EnumRanks
import phantom.models.HatStyle
import phantom.models.UserCosmetics
import phantom.models.WingsStyle
import java.util.*

/**
 * Mi. 22/12/2021 19:25
 *
 * @author cook1e
 */
object ServerInfoProvider {
    private fun getUsableUUID(uuid: String): String {
        return uuid.replace("-".toRegex(), "")
    }

    private fun getUsableUUID(uuid: UUID): String {
        return getUsableUUID(uuid.toString())
    }

    private val clientPlayers: MutableMap<String, Boolean> = HashMap()
    private val queriedPlayers = ArrayList<String>()
    fun isClientPlayer(uuid: UUID): Boolean {
        val usableUUID = getUsableUUID(uuid)
        if (clientPlayers.containsKey(usableUUID)) return clientPlayers[usableUUID]!!
        if (queriedPlayers.contains(usableUUID)) return false
        queriedPlayers.add(usableUUID)
        Thread {
            val bool = serverConnection.isPlayer(usableUUID)
            clientPlayers[usableUUID] = bool
        }.start()
        return false
    }

    private val queriedCosmeticPlayers = ArrayList<String>()
    private val cosmetics: MutableMap<String, UserCosmetics> = HashMap()
    fun getHatStyle(uuid: String): HatStyle {
        val usableUUID = getUsableUUID(uuid)
        return cosmetics[usableUUID]!!.hatStyle
    }

    fun getWingsStyle(uuid: String): WingsStyle {
        val usableUUID = getUsableUUID(uuid)
        return cosmetics[usableUUID]!!.wingsStyle
    }

    fun hasCosmetic(uuid: UUID, cosmeticStr: String?): Boolean {
        val usableUUID = getUsableUUID(uuid)
        if (cosmetics.containsKey(usableUUID)) {
            val userCosmetics = cosmetics[usableUUID] ?: return false
            return when (cosmeticStr) {
                "wings" -> userCosmetics.wingsEnabled
                "hat" -> userCosmetics.hatEnabled
                else -> false
            }
        }
        if (queriedCosmeticPlayers.contains(usableUUID)) return false
        queriedCosmeticPlayers.add(usableUUID)
        Thread {
            val cosmeticObj = serverConnection.getCosmetics(usableUUID) ?: return@Thread
            cosmetics[usableUUID] = cosmeticObj
        }.start()
        return false
    }

    private val queriedRanksPlayers = ArrayList<String>()
    private val ranks: MutableMap<String, EnumRanks> = HashMap()
    fun getRank(uuid: String): EnumRanks {
        val usableUUID = getUsableUUID(uuid)
        if (ranks.containsKey(usableUUID)) if (queriedRanksPlayers.contains(usableUUID)) return EnumRanks.NULL
        queriedRanksPlayers.add(usableUUID)
        Thread {
            val rank = serverConnection.getRank(usableUUID) ?: return@Thread
            ranks[usableUUID] = rank
        }.start()
        return EnumRanks.NULL
    }
}
