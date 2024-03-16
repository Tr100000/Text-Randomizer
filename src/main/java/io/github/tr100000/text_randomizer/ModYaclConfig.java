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
                    .binding(true, () -> TextRandomizer.modEnabled, newValue -> TextRandomizer.modEnabled = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Ignore Format Specifiers"))
                    .binding(false, () -> TextRandomizer.ignoreFormatSpecifiers, newValue -> TextRandomizer.ignoreFormatSpecifiers = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .description(OptionDescription.of(Text.literal("Only change this if you know what you're doing!")))
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .build())
            .save(TextRandomizer::saveConfig)
            .build()
            .generateScreen(parent);
    }
}
