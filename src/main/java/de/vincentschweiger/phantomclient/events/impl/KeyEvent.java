package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;
import net.minecraft.client.util.InputUtil;

public class KeyEvent extends Event {

    @Getter
    private final InputUtil.Key key;
    @Getter
    private final int i;
    @Getter
    private final int j;

    public KeyEvent(InputUtil.Key key, int i, int j) {
        this.key = key;
        this.i = i;
        this.j = j;
    }
}
