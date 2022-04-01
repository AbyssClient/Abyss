package de.vincentschweiger.phantomclient.cosmetics;

import de.vincentschweiger.phantomclient.server.ServerInfoProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class CosmeticUtils  {
    public static boolean checkClientUser(AbstractClientPlayerEntity player) {
        return ServerInfoProvider.isClientPlayer(player.getGameProfile().getId());
    }
}
