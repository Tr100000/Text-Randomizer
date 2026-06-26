package io.github.tr100000.text_randomizer.config.option;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import io.github.tr100000.text_randomizer.TextRandomizer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public abstract class GroupOption<T> extends AbstractOption<T> {
    private final List<AbstractOption<?>> options = new ObjectArrayList<>();

    protected <V extends AbstractOption<?>> V add(V option) {
        options.add(option);
        return option;
    }

    @Override
    public void reset() {
        options.forEach(AbstractOption::reset);
    }

    @Override
    public <V> DataResult<V> encode(DynamicOps<V> ops) {
        RecordBuilder<V> mapBuilder = ops.mapBuilder();
        options.forEach(option -> mapBuilder.add(option.getName(), option.encode(ops)));
        return mapBuilder.build(ops.empty());
    }

    @Override
    public <V> void decode(DynamicOps<V> ops, V input) {
        DataResult<MapLike<V>> mapLike = ops.getMap(input);
        if (mapLike.isSuccess()) {
            MapLike<V> map = mapLike.getOrThrow();
            for (AbstractOption<?> option : options) {
                if (map.get(option.getName()) != null)
                    option.decode(ops, map.get(option.getName()));
                else
                    TextRandomizer.LOGGER.warn("Map didn't contain value for \"{}\"", option.getName());
            }
        }
        else {
            TextRandomizer.LOGGER.warn("Failed to decode group \"{}\", will revert to default values", getName());
        }
    }
}
