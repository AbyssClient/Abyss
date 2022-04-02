package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.EventCancelable;
import lombok.Getter;
import lombok.Setter;

public class MouseRotationEvent extends EventCancelable {

    @Getter
    @Setter
    private double cursorDeltaX;
    @Getter
    @Setter
    private double cursorDeltaY;

    public MouseRotationEvent(double cursorDeltaX, double cursorDeltaY) {
        this.cursorDeltaX = cursorDeltaX;
        this.cursorDeltaY = cursorDeltaY;
    }
}
