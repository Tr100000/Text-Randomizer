package io.github.tr100000.text_randomizer.integration;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.LongFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import io.github.tr100000.text_randomizer.config.ModConfig;
import io.github.tr100000.text_randomizer.config.option.BooleanOption;
import io.github.tr100000.text_randomizer.config.option.LongOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.tr100000.text_randomizer.config.ModConfig.INSTANCE;

public final class ModYaclConfig {
    private ModYaclConfig() {}

    public static Screen generateScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Text Randomizer Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Settings"))
                        .option(createOption(INSTANCE.modEnabled)
                                .flag(OptionFlag.ASSET_RELOAD)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Text"))
                                .option(createOption(INSTANCE.text.randomize)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .build())
                                .option(createOption(INSTANCE.text.ignoreEmptyStrings)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .description(OptionDescription.of(Component.literal("Ignore empty strings (disable for more chaos)")))
                                        .build())
                                .option(createOption(INSTANCE.text.ignoreFormatSpecifiers)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .description(OptionDescription.of(Component.literal("Not recommended, enable if you hate yourself")))
                                        .build())
                                .build()
                        )
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Items"))
                                //? if >=1.21.11 {
                                .option(createOption(INSTANCE.items.randomizeModels)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .build())
                                //?}
                                .option(createOption(INSTANCE.items.hideIds)
                                        .build())
                                .option(createOption(INSTANCE.items.shuffleItemGroups)
                                        .build())
                                .build()
                        )
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Seed"))
                                .collapsed(true)
                                .option(createOption(INSTANCE.seed.useSeed)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .build())
                                .option(createOption(INSTANCE.seed.seed)
                                        .flag(OptionFlag.ASSET_RELOAD)
                                        .build())
                                .build()
                        )
                        .build()
                )
                .save(ModConfig::save)
                .build()
                .generateScreen(parent);
    }

    private static Option.Builder<?> createOption(BooleanOption option) {
        return Option.<Boolean>createBuilder()
                .name(option.getTitle())
                .binding(option.getDefaultValue(), option::getValue, option::setValue)
                .controller(TickBoxControllerBuilder::create);
    }

    private static Option.Builder<?> createOption(LongOption option) {
        return Option.<Long>createBuilder()
                .name(option.getTitle())
                .binding(option.getDefaultValue(), option::getValue, option::setValue)
                .controller(LongFieldControllerBuilder::create);
    }
}
