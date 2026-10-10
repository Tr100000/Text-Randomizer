package io.github.tr100000.text_randomizer.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.tr100000.text_randomizer.ModLoaderAccess;
import io.github.tr100000.text_randomizer.TextRandomizer;
import io.github.tr100000.text_randomizer.config.option.BooleanOption;
import io.github.tr100000.text_randomizer.config.option.GroupOption;
import io.github.tr100000.text_randomizer.config.option.LongOption;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ModConfig extends GroupOption<ModConfig> {
    public static final ModConfig INSTANCE = new ModConfig();

    private ModConfig() {}

    public final BooleanOption modEnabled = add(new BooleanOption(true, "mod_enabled", Component.literal("Mod Enabled")));
    public final TextSettings text = add(new TextSettings());
    public final ItemSettings items = add(new ItemSettings());
    public final TextureSettings textures = add(new TextureSettings());
    public final SeedSettings seed = add(new SeedSettings());

    public static class TextSettings extends GroupOption<TextSettings> {
        public final BooleanOption randomize = add(new BooleanOption(true, "randomize", Component.literal("Randomize Text")));
        public final BooleanOption ignoreEmptyStrings = add(new BooleanOption(true ,"ignore_empty_strings", Component.literal("Ignore Empty Strings")));
        public final BooleanOption ignoreFormatSpecifiers = add(new BooleanOption(false, "ignore_format_specifiers", Component.literal("Ignore Format Specifiers")));

        @Override
        public String getName() {
            return "text";
        }
    }

    public static class ItemSettings extends GroupOption<ItemSettings> {
        //? if >=1.21.11
        public final BooleanOption randomizeModels = add(new BooleanOption(false, "randomize_models", Component.literal("Randomize Item Models")));
        public final BooleanOption hideIds = add(new BooleanOption(true ,"hide_item_ids", Component.literal("Hide Item Ids")));
        public final BooleanOption shuffleItemGroups = add(new BooleanOption(false, "shuffle_item_groups", Component.literal("Shuffle Item Groups")));

        @Override
        public String getName() {
            return "items";
        }
    }

    public static class TextureSettings extends GroupOption<TextureSettings> {
        public final BooleanOption shuffleAtlasTextures = add(new BooleanOption(false, "shuffle_atlas_textures", Component.literal("Shuffle Atlas Textures")));

        @Override
        public String getName() {
            return "textures";
        }
    }

    public static class SeedSettings extends GroupOption<SeedSettings> {
        public final BooleanOption useSeed = add(new BooleanOption(false, "use_seed", Component.literal("Use Seed")));
        public final LongOption seed = add(new LongOption(0, "seed", Component.literal("Seed")));

        @Override
        public String getName() {
            return "seed";
        }
    }

    public static boolean should(Predicate<ModConfig> predicate) {
        return INSTANCE.modEnabled.getValue() && predicate.test(INSTANCE);
    }

    public static boolean check(Function<ModConfig, BooleanOption> predicate) {
        return INSTANCE.modEnabled.getValue() && predicate.apply(INSTANCE).getValue();
    }

    @Override
    public String getName() {
        throw new IllegalStateException();
    }

    @ApiStatus.Internal
    public static void load() {
        try {
            if (Files.notExists(TextRandomizer.CONFIG_PATH)) {
                TextRandomizer.LOGGER.info("Config file not found");
                save();
                return;
            }

            try (JsonReader jsonReader = new JsonReader(Files.newBufferedReader(TextRandomizer.CONFIG_PATH))) {
                JsonElement json = JsonParser.parseReader(jsonReader);
                INSTANCE.decode(JsonOps.INSTANCE, json);
            }

            TextRandomizer.LOGGER.info("Loaded config file");

            save();
        }
        catch (IOException e) {
            TextRandomizer.LOGGER.error("Failed to load config file", e);
        }
    }

    @ApiStatus.Internal
    public static void save() {
        try {
            Files.deleteIfExists(TextRandomizer.CONFIG_PATH);

            DataResult<JsonElement> jsonResult = INSTANCE.encode(JsonOps.INSTANCE);
            if (jsonResult.isSuccess()) {
                JsonElement json = jsonResult.getOrThrow();
                Files.writeString(TextRandomizer.CONFIG_PATH, TextRandomizer.GSON.toJson(json));
            }
            else {
                TextRandomizer.LOGGER.error("Failed to encode config");
            }
        }
        catch (IOException e) {
            TextRandomizer.LOGGER.error("Failed to save config file", e);
        }
    }

    public static boolean canUseYaclConfig() {
        return ModLoaderAccess.INSTANCE.isModLoaded("yet_another_config_lib_v3");
    }
}
