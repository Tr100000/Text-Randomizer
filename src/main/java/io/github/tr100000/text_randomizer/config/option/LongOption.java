package io.github.tr100000.text_randomizer.config.option;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.network.chat.Component;

public class LongOption extends SimpleOption<Long> {
    public LongOption(long defaultValue, String name, Component title) {
        super(defaultValue, name, title);
    }

    @Override
    public <V> DataResult<V> encode(DynamicOps<V> ops) {
        return DataResult.success(ops.createLong(getValue()));
    }

    @Override
    public <V> void decode(DynamicOps<V> ops, V input) {
        ops.getNumberValue(input).ifSuccess(n -> setValue((long)n));
    }
}
