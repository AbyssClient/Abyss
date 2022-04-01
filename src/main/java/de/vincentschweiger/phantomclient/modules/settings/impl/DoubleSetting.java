package de.vincentschweiger.phantomclient.modules.settings.impl;

public class DoubleSetting extends Setting<Double> {
    public DoubleSetting(double x) {
        super(x);
    }

    @Override
    public Double defaultValue() {
        return 0.0;
    }

    @Override
    protected String getType() {
        return "double";
    }
}
