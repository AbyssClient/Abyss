package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.EventCancelable;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;

public class ScreenEvent extends EventCancelable {

    @Getter
    private final Screen screen;

    public ScreenEvent(Screen screen) {
        this.screen = screen;
    }
}
