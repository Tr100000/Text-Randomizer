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
                    .name(Text.literal("Randomize Text"))
                    .binding(true, () -> TextRandomizer.modEnabled, newValue -> TextRandomizer.modEnabled = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Export Language File"))
                    .binding(false, () -> TextRandomizer.exportLanguage, newValue -> TextRandomizer.exportLanguage = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .description(OptionDescription.of(Text.literal("Export the randomized text as a language file")))
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Ignore Format Specifiers"))
                    .binding(false, () -> TextRandomizer.ignoreFormatSpecifiers, newValue -> TextRandomizer.ignoreFormatSpecifiers = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .description(OptionDescription.of(Text.literal("Chaos mode")))
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .build())
            .save(TextRandomizer::saveConfig)
            .build()
            .generateScreen(parent);
    }
}
