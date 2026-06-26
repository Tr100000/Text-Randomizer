package io.github.tr100000.text_randomizer.mixins;

import io.github.tr100000.text_randomizer.Shuffle;
import io.github.tr100000.text_randomizer.config.ModConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ClientLanguage.class)
public abstract class ClientLanguageMixin {
    @ModifyVariable(method = "<init>*", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static Map<String, String> shuffle(Map<String, String> storage) {
        if (ModConfig.check(c -> c.text.randomize)) {
            Map<String, String> shuffledStorage = new Object2ObjectOpenHashMap<>();

            Map<String, String> pool;
            if (ModConfig.INSTANCE.text.ignoreEmptyStrings.getValue()) {
                pool = new HashMap<>();
                for (Map.Entry<String, String> entry : storage.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        shuffledStorage.put(entry.getKey(), entry.getValue());
                    }
                    else {
                        pool.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            else {
                pool = storage;
            }

            if (ModConfig.INSTANCE.text.ignoreFormatSpecifiers.getValue()) {
                shuffledStorage.putAll(Shuffle.shuffleMap(pool));
            }
            else {
                Map<Integer, Map<String, String>> seperatedMap = new Object2ObjectOpenHashMap<>();
                pool.forEach((key, value) -> {
                    int count = value.split("%(?:\\d+\\$)?[a-z]", -1).length;
                    seperatedMap.computeIfAbsent(count, HashMap::new);
                    seperatedMap.get(count).put(key, value);
                });

                seperatedMap.forEach((count, map) -> shuffledStorage.putAll(Shuffle.shuffleMap(map)));
            }

            return shuffledStorage;
        }
        else {
            return storage;
        }
    }
}
