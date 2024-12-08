package io.github.tr100000.text_randomizer;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.LongFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
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
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Text"))
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Randomize Text"))
                        .binding(true, () -> TextRandomizer.randomizeText, newValue -> TextRandomizer.randomizeText = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Ignore Format Specifiers"))
                        .binding(false, () -> TextRandomizer.ignoreFormatSpecifiers, newValue -> TextRandomizer.ignoreFormatSpecifiers = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .description(OptionDescription.of(Text.literal("Chaos mode")))
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Items"))
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Randomize Item Models"))
                        .binding(false, () -> TextRandomizer.randomizeItemModels, newValue -> TextRandomizer.randomizeItemModels = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Hide Item Ids"))
                        .binding(false, () -> TextRandomizer.hideItemIds, newValue -> TextRandomizer.hideItemIds = newValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Seed"))
                    .collapsed(true)
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Use Seed"))
                        .binding(false, () -> TextRandomizer.useSeed, newValue -> TextRandomizer.useSeed = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Long>createBuilder()
                        .name(Text.literal("Seed"))
                        .binding(0L, () -> TextRandomizer.seed, newValue -> TextRandomizer.seed = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(LongFieldControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Text.literal("Export"))
                    .collapsed(true)
                    .option(Option.<Boolean>createBuilder()
                        .name(Text.literal("Export To Resource Pack"))
                        .binding(false, () -> TextRandomizer.exportToResourcePack, newValue -> TextRandomizer.exportToResourcePack = newValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<String>createBuilder()
                        .name(Text.literal("Generated Pack Name"))
                        .binding("generated", () -> TextRandomizer.generatedPackName, newValue -> TextRandomizer.generatedPackName = newValue)
                        .controller(StringControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .build()
            )
            .save(TextRandomizer::saveConfig)
            .build()
            .generateScreen(parent);
    }
}
