package io.github.tr100000.text_randomizer;

import io.github.tr100000.text_randomizer.config.ModConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Shuffle {
    private Shuffle() {}

    public static <T, E> Map<T, E> shuffleMap(Map<T, E> original) {
        Map<T, E> shuffled = new Object2ObjectOpenHashMap<>();

        List<T> originalKeys = new ObjectArrayList<>(original.keySet());
        List<T> shuffledKeys = shuffleList(new ObjectArrayList<>(originalKeys));

        originalKeys.forEach(key -> shuffled.put(key, original.get(key)));

        for (int i = 0; i < originalKeys.size(); i++) {
            shuffled.put(originalKeys.get(i), original.get(shuffledKeys.get(i)));
        }

        return shuffled;
    }

    public static <T> List<T> shuffleList(List<T> list) {
        Collections.shuffle(list, getRandom());
        return list;
    }

    public static Random getRandom() {
        return ModConfig.INSTANCE.seed.useSeed.getValue() ? new Random(ModConfig.INSTANCE.seed.seed.getValue()) : new Random();
    }
}
