package de.vincentschweiger.phantomclient.events.impl;

import de.vincentschweiger.phantomclient.events.Event;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class ScreenRenderEvent extends Event {

    @Getter
    private final Screen screen;
    @Getter
    private final MatrixStack matrixStack;
    @Getter
    private final int mouseX;
    @Getter
    private final int mouseY;
    @Getter
    private final float delta;

    public ScreenRenderEvent(Screen screen, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.screen = screen;
        this.matrixStack = matrices;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.delta = delta;
    }
}
