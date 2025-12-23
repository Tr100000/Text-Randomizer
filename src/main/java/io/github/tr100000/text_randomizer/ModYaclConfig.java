package io.github.tr100000.text_randomizer;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.LongFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ModYaclConfig {
    private ModYaclConfig() {}

    public static Screen generateScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Text Randomizer Config"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("Settings"))
                .option(Option.<Boolean>createBuilder()
                    .name(Component.literal("Mod Enabled"))
                    .binding(true, () -> ModConfig.INSTANCE.modEnabled, newValue -> ModConfig.INSTANCE.modEnabled = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("Text"))
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Randomize Text"))
                        .binding(true, () -> ModConfig.INSTANCE.randomizeText, newValue -> ModConfig.INSTANCE.randomizeText = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Ignore Empty Strings"))
                        .binding(true, () -> ModConfig.INSTANCE.ignoreEmptyStrings, newValue -> ModConfig.INSTANCE.ignoreEmptyStrings = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .description(OptionDescription.of(Component.literal("Ignore empty strings (disable for more chaos)")))
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Ignore Format Specifiers"))
                        .binding(false, () -> ModConfig.INSTANCE.ignoreFormatSpecifiers, newValue -> ModConfig.INSTANCE.ignoreFormatSpecifiers = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .description(OptionDescription.of(Component.literal("Not recommended, enable if you hate yourself")))
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("Items"))
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Randomize Item Models"))
                        .binding(false, () -> ModConfig.INSTANCE.randomizeItemModels, newValue -> ModConfig.INSTANCE.randomizeItemModels = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Hide Item Ids"))
                        .binding(true, () -> ModConfig.INSTANCE.hideItemIds, newValue -> ModConfig.INSTANCE.hideItemIds = newValue)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .group(OptionGroup.createBuilder()
                    .name(Component.literal("Seed"))
                    .collapsed(true)
                    .option(Option.<Boolean>createBuilder()
                        .name(Component.literal("Use Seed"))
                        .binding(false, () -> ModConfig.INSTANCE.useSeed, newValue -> ModConfig.INSTANCE.useSeed = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(TickBoxControllerBuilder::create)
                        .build()
                    )
                    .option(Option.<Long>createBuilder()
                        .name(Component.literal("Seed"))
                        .binding(0L, () -> ModConfig.INSTANCE.seed, newValue -> ModConfig.INSTANCE.seed = newValue)
                        .flag(OptionFlag.ASSET_RELOAD)
                        .controller(LongFieldControllerBuilder::create)
                        .build()
                    )
                    .build()
                )
                .build()
            )
            .save(ModConfig::save)
            .build()
            .generateScreen(parent);
    }
}
