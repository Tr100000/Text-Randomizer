package io.github.tr100000.text_randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Shuffle {
    private Shuffle() {}

    public static <T, E> Map<T, E> shuffleMap(Map<T, E> original) {
        Map<T, E> shuffled = new HashMap<>();

        List<T> originalKeys = new ArrayList<>(original.keySet());
        List<T> shuffledKeys = shuffleList(new ArrayList<>(originalKeys));

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
        return ModConfig.INSTANCE.useSeed ? new Random(ModConfig.INSTANCE.seed) : new Random();
    }
}
