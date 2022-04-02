package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class MouseButtonEvent extends Event {

    @Getter
    private final long window;
    @Getter
    private final int button;
    @Getter
    private final int action;
    @Getter
    private final int mods;

    public MouseButtonEvent(long window, int button, int action, int mods) {
        this.window = window;
        this.button = button;
        this.action = action;
        this.mods = mods;
    }
}
