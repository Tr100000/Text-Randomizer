package io.github.tr100000.text_randomizer.mixins;

import io.github.tr100000.text_randomizer.ExportUtils;
import io.github.tr100000.text_randomizer.ModConfig;
import io.github.tr100000.text_randomizer.Shuffle;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ClientLanguage.class)
public abstract class ClientLanguageMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static Map<String, String> shuffle(Map<String, String> original) {
        if (ModConfig.should(c -> c.randomizeText)) {
            Map<String, String> shuffled = new HashMap<>();

            Map<String, String> pool;
            if (ModConfig.INSTANCE.ignoreEmptyStrings) {
                pool = new HashMap<>();
                for (Map.Entry<String, String> entry : original.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        shuffled.put(entry.getKey(), entry.getValue());
                    }
                    else {
                        pool.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            else {
                pool = original;
            }

            if (ModConfig.INSTANCE.ignoreFormatSpecifiers) {
                shuffled.putAll(Shuffle.shuffleMap(pool));
            }
            else {
                Map<Integer, Map<String, String>> seperatedMap = new HashMap<>();
                pool.forEach((key, value) -> {
                    int count = value.split("%(\\d\\$)?s", -1).length;
                    seperatedMap.computeIfAbsent(count, HashMap::new);
                    seperatedMap.get(count).put(key, value);
                });

                seperatedMap.forEach((count, map) -> {
                    List<String> originalKeys = new ArrayList<>(map.keySet());
                    List<String> shuffledKeys = Shuffle.shuffleList(new ArrayList<>(originalKeys));

                    for (int i = 0; i < originalKeys.size(); i++) {
                        shuffled.put(originalKeys.get(i), map.get(shuffledKeys.get(i)));
                    }
                });
            }

            ExportUtils.trySaveLanguage(shuffled);
            return shuffled;
        }
        else {
            if (ModConfig.should(c -> c.exportAll)) {
                ExportUtils.trySaveLanguage(original);
            }

            return original;
        }
    }
}
