package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class MouseCursorEvent extends Event {
    @Getter
    private final long window;
    @Getter
    private final double x;
    @Getter
    private final double y;

    public MouseCursorEvent(long window, double x, double y) {
        this.window = window;
        this.y = y;
        this.x = x;
    }
}
