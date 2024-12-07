package io.github.tr100000.text_randomizer.mixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.tr100000.text_randomizer.Shuffle;
import io.github.tr100000.text_randomizer.TextRandomizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.resource.language.TranslationStorage;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Map<String, String> shuffle(Map<String, String> original) {
        if (TextRandomizer.randomizeText) {
            Map<String, String> shuffled;

            if (TextRandomizer.ignoreFormatSpecifiers) {
                shuffled = Shuffle.shuffleMap(original);
            }
            else {
                shuffled = new HashMap<>();
                Map<Integer, Map<String, String>> seperatedMap = new HashMap<>();
                original.entrySet().forEach(entry -> {
                    int count = entry.getValue().split("%(\\d\\$)?s", -1).length;
                    seperatedMap.computeIfAbsent(count, HashMap::new);
                    seperatedMap.get(count).put(entry.getKey(), entry.getValue());
                });

                seperatedMap.forEach((count, map) -> {
                    List<String> originalKeys = new ArrayList<>(map.keySet());
                    List<String> shuffledKeys = Shuffle.shuffleList(new ArrayList<>(originalKeys));

                    for (int i = 0; i < originalKeys.size(); i++) {
                        shuffled.put(originalKeys.get(i), map.get(shuffledKeys.get(i)));
                    }
                });
            }

            TextRandomizer.trySaveLanguage(shuffled);
            return shuffled;
        }
        else {
            TextRandomizer.trySaveLanguage(original);
            return original;
        }
    }
}
