package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class MouseScrollEvent extends Event {

    @Getter
    private final long window;
    @Getter
    private final double horizontal;
    @Getter
    private final double vertical;

    public MouseScrollEvent(long window, double horizontal, double vertical) {
        this.window = window;
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
}
