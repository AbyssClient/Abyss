package de.vincentschweiger.phantomclient.server;

import de.vincentschweiger.phantomclient.Mod;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.EnumUtils;
import phantom.models.EnumRanks;
import phantom.models.UserCosmetics;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Mi. 22/12/2021 16:09
 *
 * @author cook1e
 */
public class ServerConnection {
    private boolean running = true;
    private String host = "phantom.vincentschweiger.de";
    private int port = 19623;
    private DataOutputStream os;
    private DataInputStream is;
    private Socket socket;

    public void reconnect() {
        try {
            socket = new Socket(host, port);
            os = new DataOutputStream((socket.getOutputStream()));
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerConnection() {
        try {
            socket = new Socket(host, port);
            os = new DataOutputStream((socket.getOutputStream()));
            is = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        try {
            String meMsg = "ME " + MinecraftClient.getInstance().getSession().getUuid().replaceAll("-", "");
            os.writeInt(meMsg.length());
            os.write(meMsg.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayer(String uuid) {
        try {
            String msg = "IS " + uuid;
            os.writeInt(msg.length());
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
            return is.readBoolean();
        } catch (SocketException e) {
            reconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    String getStringRepresentation(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public UserCosmetics getCosmetics(String uuid) {
        try {
            String msg = "GET " + uuid;
            os.writeInt(msg.length());
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
            int length = is.readInt();
            byte[] buff = new byte[length];
            is.readFully(buff);
            String s = new String(buff);
            return Mod.getInstance().getGson().fromJson(s, UserCosmetics.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EnumRanks getRank(String uuid) {
        try {
            String msg = "RANK " + uuid;
            os.writeInt(msg.length());
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
            int length = is.readInt();
            char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = is.readChar();
            }
            String rank = new String(chars);
            return EnumUtils.getEnum(EnumRanks.class, rank);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void shutdown() {
        try {
            running = false;
            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}