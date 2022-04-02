package de.vincentschweiger.phantomclient.cosmetics

import de.vincentschweiger.phantomclient.socket.ServerInfoProvider.isClientPlayer
import net.minecraft.client.network.AbstractClientPlayerEntity

object CosmeticUtils {
    fun checkClientUser(player: AbstractClientPlayerEntity): Boolean {
        return isClientPlayer(player.gameProfile.id)
    }
}