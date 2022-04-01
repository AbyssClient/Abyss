package de.vincentschweiger.phantomclient.modules.impl;

import de.vincentschweiger.phantomclient.mixins.MinecraftClientAccessor;
import de.vincentschweiger.phantomclient.modules.UIModule;
import net.minecraft.client.MinecraftClient;

import java.awt.*;

public class ModuleFPS extends UIModule {

    @Override
    public String getText() {
        int fps = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getCurrentFps();
        // FPS: 999
        // 999 FPS
        return this.getState() == 0 ? "FPS: " + fps  : fps + " FPS";
    }

    @Override
    public Color getColor() {
        return new Color(255, 100, 0);
    }

    @Override
    public String getName() {
        return "FPS";
    }

    @Override
    public int getMaxState() {
        return 1;
    }
}