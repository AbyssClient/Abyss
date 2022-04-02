package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class WindowFocusEvent extends Event {

    @Getter
    private final boolean focused;
    @Getter
    private final long window;

    public WindowFocusEvent(long window, boolean focused) {
        this.window = window;
        this.focused = focused;
    }
}
