package de.vincentschweiger.phantomclient.server;

import de.vincentschweiger.phantomclient.Mod;
import net.minecraft.client.MinecraftClient;
import phantom.models.EnumRanks;
import phantom.models.HatStyle;
import phantom.models.UserCosmetics;
import phantom.models.WingsStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mi. 22/12/2021 19:25
 *
 * @author cook1e
 */
public class ServerInfoProvider {
    private static String getUsableUUID(String uuid) {
        return uuid.replaceAll("-", "");
    }
    private static String getUsableUUID(UUID uuid) {
        return getUsableUUID(uuid.toString());
    }

    private static final Map<String, Boolean> clientPlayers = new HashMap<>();
    private static final ArrayList<String> queriedPlayers = new ArrayList<>();

    public static boolean isClientPlayer(UUID uuid) {
        String usableUUID = getUsableUUID(uuid);
        if (clientPlayers.containsKey(usableUUID)) return clientPlayers.get(usableUUID);
        if (queriedPlayers.contains(usableUUID)) return false;
        queriedPlayers.add(usableUUID);
        new Thread(() -> {
            Boolean bool = Mod.getInstance().getServerConnection().isPlayer(usableUUID);
            clientPlayers.put(usableUUID, bool);
        }).start();
        return false;
    }

    private static final ArrayList<String> queriedCosmeticPlayers = new ArrayList<>();
    private static final Map<String, UserCosmetics> cosmetics = new HashMap<>();

    public static HatStyle getHatStyle(String uuid) {
        String usableUUID = getUsableUUID(uuid);
        return cosmetics.get(usableUUID).hatStyle;
    }

    public static WingsStyle getWingsStyle(String uuid) {
        String usableUUID = getUsableUUID(uuid);
        return cosmetics.get(usableUUID).wingsStyle;
    }

    public static boolean hasCosmetic(UUID uuid, String cosmeticStr) {
        String usableUUID = getUsableUUID(uuid);
        if (cosmetics.containsKey(usableUUID)) {
            UserCosmetics userCosmetics = cosmetics.get(usableUUID);
            if (userCosmetics == null) return false;
            switch (cosmeticStr) {
                case "wings":
                    return userCosmetics.wingsEnabled;
                case "hat":
                    return userCosmetics.hatEnabled;
                default:
                    return false;
            }
        }
        if (queriedCosmeticPlayers.contains(usableUUID)) return false;
        queriedCosmeticPlayers.add(usableUUID);
        new Thread(() -> {
            UserCosmetics cosmeticObj = Mod.getInstance().getServerConnection().getCosmetics(usableUUID);
            cosmetics.put(usableUUID, cosmeticObj);
        }).start();
        return false;
    }


    private static final ArrayList<String> queriedRanksPlayers = new ArrayList<>();
    private static final Map<String, EnumRanks> ranks = new HashMap<>();

    public static EnumRanks getRank(String uuid, String cosmetic) {
        String usableUUID = getUsableUUID(uuid);
        if (ranks.containsKey(usableUUID))
            if (queriedRanksPlayers.contains(usableUUID)) return EnumRanks.NULL;
        queriedRanksPlayers.add(usableUUID);
        new Thread(() -> {
            EnumRanks rank = Mod.getInstance().getServerConnection().getRank(usableUUID);
            ranks.put(usableUUID, rank);
        }).start();
        return EnumRanks.NULL;
    }

}