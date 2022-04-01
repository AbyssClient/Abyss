package de.vincentschweiger.phantomclient.modules.impl;

import de.vincentschweiger.phantomclient.modules.UIModule;
import net.minecraft.client.render.block.BlockModelRenderer;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModuleClock extends UIModule {
    @Override
    public String getName() {
        return "Clock";
    }

    @Override
    public String getText() {
        SimpleDateFormat formatter = new SimpleDateFormat("[HH:mm]");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    @Override
    public Color getColor() {
        return new Color(255, 100, 0);
    }
}
