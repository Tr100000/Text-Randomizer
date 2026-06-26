package io.github.tr100000.text_randomizer.config.option;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public abstract class AbstractOption<T> {
    public abstract String getName();

    public abstract void reset();

    public abstract <V> DataResult<V> encode(DynamicOps<V> ops);
    public abstract <V> void decode(DynamicOps<V> ops, V input);
}
