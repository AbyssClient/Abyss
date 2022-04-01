package de.vincentschweiger.phantomclient;

import com.google.gson.Gson;
import de.vincentschweiger.phantomclient.cosmetics.hat.HatModel;
import de.vincentschweiger.phantomclient.cosmetics.hat.HatRenderer;
import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsModel;
import de.vincentschweiger.phantomclient.cosmetics.dragonwings.DragonwingsRenderer;
import de.vincentschweiger.phantomclient.events.EventManager;
import de.vincentschweiger.phantomclient.modules.ModulePositionScreen;
import de.vincentschweiger.phantomclient.modules.Modules;
import de.vincentschweiger.phantomclient.modules.UIModule;
import de.vincentschweiger.phantomclient.modules.impl.ModuleClock;
import de.vincentschweiger.phantomclient.modules.impl.ModuleFPS;
import de.vincentschweiger.phantomclient.server.ServerConnection;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Mod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("phantom");

    private static Mod instance;
    KeyBinding kb;
    @Getter
    private static Gson gson = new Gson();
    @Getter
    private static ServerConnection serverConnection;
    public static File CONFIG_DIR = new File("phantom");

    @Override
    public void onInitialize() {
        this.kb = KeyBindingHelper.registerKeyBinding(new KeyBinding("phantom.keybinding.positioning", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Phantom"));
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (kb.wasPressed()) MinecraftClient.getInstance().setScreen(new ModulePositionScreen());
        });
        serverConnection = new ServerConnection();
        serverConnection.setup();
        EntityModelLayerRegistry.registerModelLayer(HatRenderer.LAYER, HatModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DragonwingsRenderer.LAYER, DragonwingsModel::getTexturedModelData);
        LOGGER.info("Phantom has started!");
    }

    public void shutdown() {
        Modules.getRegisteredModules().forEach(UIModule::save);
        LOGGER.info("Phantom shut down!");
    }

    public void postInit() {
        Modules.getRegisteredModules().forEach(UIModule::load);
    }

    public void preInit() {
        EventManager.register(new de.vincentschweiger.phantomclient.listeners.EventListener());
        Modules.registerModules(new ModuleFPS());
        Modules.registerModules(new ModuleClock());
    }

    public static Mod getInstance() {
        if (instance == null) instance = new Mod();
        return instance;
    }
}