package io.github.tr100000.text_randomizer.integration;

//? if fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.tr100000.text_randomizer.config.ModConfig;

public class TextRandomizerModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig.canUseYaclConfig() ? ModYaclConfig::generateScreen : ModMenuApi.super.getModConfigScreenFactory();
    }
}
//?}
