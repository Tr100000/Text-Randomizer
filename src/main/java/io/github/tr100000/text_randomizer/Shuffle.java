package io.github.tr100000.text_randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Shuffle {
    private Shuffle() {}

    public static <T, E> Map<T, E> shuffleMap(Map<T, E> original) {
        Map<T, E> shuffled = new HashMap<>();

        List<T> originalKeys = new ArrayList<>(original.keySet());
        List<T> shuffledKeys = new ArrayList<>(originalKeys);
        Collections.shuffle(shuffledKeys);

        for (int i = 0; i < originalKeys.size(); i++) {
            shuffled.put(originalKeys.get(i), original.get(shuffledKeys.get(i)));
        }

        return shuffled;
    }

    public static <T, E> Map<T, E> shuffleMapIf(boolean shouldShuffle, Map<T, E> original) {
        return shouldShuffle ? shuffleMap(original) : original;
    }
}
