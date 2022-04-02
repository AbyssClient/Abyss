package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class WindowResizeEvent extends Event {

    @Getter
    private final long window;
    @Getter
    private final int width;
    @Getter
    private final int height;

    public WindowResizeEvent(long window, int width, int height) {
        this.window = window;
        this.width = width;
        this.height = height;
    }
}
