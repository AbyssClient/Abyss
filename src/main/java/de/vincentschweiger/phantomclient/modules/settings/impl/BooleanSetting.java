package de.vincentschweiger.phantomclient.modules.settings.impl;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(boolean newVal) {
        value = newVal;
    }

    @Override

    public Boolean defaultValue() {
        return false;
    }

    @Override
    protected String getType() {
        return "bool";
    }

    public void toggle() {
        value = !value;
    }
}
