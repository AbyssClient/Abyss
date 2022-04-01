package de.vincentschweiger.phantomclient.listeners;

import de.vincentschweiger.phantomclient.events.EventAnnotation;
import de.vincentschweiger.phantomclient.events.impl.RenderOverlayEvent;
import de.vincentschweiger.phantomclient.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class EventListener {

    @EventAnnotation
    public void onRenderOverlay(RenderOverlayEvent e) {
        if ((MinecraftClient.getInstance().currentScreen == null || MinecraftClient.getInstance().currentScreen instanceof ChatScreen || MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) && !MinecraftClient.getInstance().options.debugEnabled)
            Modules.getRegisteredModules().forEach(m -> {
                if (m.isEnabled()) m.render(true);
            });
    }
}