package io.github.tr100000.text_randomizer;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class TextRandomizerModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig.canUseYaclConfig() ? ModYaclConfig::generateScreen : screen -> null;
    }
}
