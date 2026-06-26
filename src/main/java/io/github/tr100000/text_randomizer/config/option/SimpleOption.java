package io.github.tr100000.text_randomizer.config.option;

import net.minecraft.network.chat.Component;

public abstract class SimpleOption<T> extends AbstractOption<T> {
    protected final String name;
    protected final Component title;
    protected final T defaultValue;
    protected T value;

    protected SimpleOption(T defaultValue, String name, Component title) {
        this.name = name;
        this.title = title;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public Component getTitle() {
        return title;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        value = newValue;
    }

    @Override
    public void reset() {
        setValue(getDefaultValue());
    }
}
