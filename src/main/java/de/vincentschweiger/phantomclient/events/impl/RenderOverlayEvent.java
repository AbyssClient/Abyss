package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;

public class RenderOverlayEvent extends Event {
    @Getter
    private final MatrixStack matrixStack;

    public RenderOverlayEvent(MatrixStack matrices) {
        this.matrixStack = matrices;
    }

}