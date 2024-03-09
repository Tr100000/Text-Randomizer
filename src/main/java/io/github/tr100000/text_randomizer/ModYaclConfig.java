package io.github.tr100000.text_randomizer;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class ModYaclConfig {
    private ModYaclConfig() {}

    public static Screen generateScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
            .title(Text.literal("Text Randomizer Config"))
            .category(ConfigCategory.createBuilder()
                .name(Text.literal("Settings"))
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Mod Enabled"))
                    .binding(true, () -> ModConfig.INSTANCE.modEnabled.value(), newValue -> ModConfig.INSTANCE.modEnabled.setValue(newValue, true))
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Ignore Format Specifiers"))
                    .binding(false, () -> ModConfig.INSTANCE.ignoreFormatSpecifiers.value(), newValue -> ModConfig.INSTANCE.ignoreFormatSpecifiers.setValue(newValue, true))
                    .flag(OptionFlag.ASSET_RELOAD)
                    .description(OptionDescription.of(Text.literal("Only change this if you know what you're doing!")))
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .build())
            .save(ModConfig.INSTANCE::save)
            .build()
            .generateScreen(parent);
    }
}
