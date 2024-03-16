package io.github.tr100000.text_randomizer.mixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.tr100000.text_randomizer.TextRandomizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.resource.language.TranslationStorage;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Map<String, String> oops(Map<String, String> translations) {
        if (TextRandomizer.modEnabled) {
			Map<String, String> shuffled = new HashMap<>();

			if (TextRandomizer.ignoreFormatSpecifiers) {
				List<String> originalKeys = new ArrayList<>(translations.keySet());
				List<String> shuffledKeys = new ArrayList<>(originalKeys);
				Collections.shuffle(shuffledKeys);
				
				for (int i = 0; i < originalKeys.size(); i++) {
					shuffled.put(originalKeys.get(i), translations.get(shuffledKeys.get(i)));
				}
			}
			else {
				Map<Integer, Map<String, String>> seperatedMap = new HashMap<>();
				translations.entrySet().forEach(entry -> {
					int count = entry.getValue().split("%(\\d\\$)?s", -1).length;
					seperatedMap.computeIfAbsent(count, HashMap::new);
					seperatedMap.get(count).put(entry.getKey(), entry.getValue());
				});
				
				seperatedMap.forEach((count, map) -> {
					List<String> originalKeys = new ArrayList<>(map.keySet());
					List<String> shuffledKeys = new ArrayList<>(originalKeys);
					Collections.shuffle(shuffledKeys);
					
					for (int i = 0; i < originalKeys.size(); i++) {
						shuffled.put(originalKeys.get(i), map.get(shuffledKeys.get(i)));
					}
				});
			}
			
			TextRandomizer.saveLanguage(shuffled);
			return shuffled;
		}
		else {
			TextRandomizer.saveLanguage(translations);
			return translations;
		}
    }
}
