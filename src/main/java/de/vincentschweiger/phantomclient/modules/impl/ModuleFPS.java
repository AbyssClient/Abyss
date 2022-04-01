package de.vincentschweiger.phantomclient.modules.impl;

import de.vincentschweiger.phantomclient.modules.UIModule;

import java.awt.*;

public class ModuleFPS extends UIModule {

    @Override
    public String getText() {
        // FPS: 999
        // 999 FPS
        return this.getState() == 0 ? "FPS: " + 999 : 999 + " FPS";
    }

    @Override
    public Color getColor() {
        return new Color(255, 100, 0);
    }

    @Override
    public int getMaxState() {
        return 1;
    }
}