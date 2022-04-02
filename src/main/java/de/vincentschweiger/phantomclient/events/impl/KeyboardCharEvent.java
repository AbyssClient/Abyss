package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class KeyboardCharEvent extends Event {

    @Getter
    private final long window;
    // i = the char
    @Getter
    private final int i;

    public KeyboardCharEvent(long window, int i) {
        this.window = window;
        this.i = i;
    }
}
