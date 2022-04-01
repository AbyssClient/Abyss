package de.vincentschweiger.phantomclient.modules.settings.impl;

public abstract class Setting<T> {
    protected T value;
    protected String type = getType();

    public Setting() {}

    public Setting(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        if (value == null) return defaultValue();
        return value;
    }

    public void set(T newValue) {
        value = newValue;
    }

    protected abstract String getType();

    public abstract T defaultValue();
}
