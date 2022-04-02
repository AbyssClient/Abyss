package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;

public class KeyboardKeyEvent extends Event {
    @Getter
    private final long window;
    @Getter
    private final int key;
    @Getter
    private final int scancode;
    @Getter
    private final int i;
    @Getter
    private final int j;

    public KeyboardKeyEvent(long window, int key, int scancode, int i, int j) {
        this.window = window;
        this.key = key;
        this.scancode = scancode;
        this.i = i;
        this.j = j;
    }
}
