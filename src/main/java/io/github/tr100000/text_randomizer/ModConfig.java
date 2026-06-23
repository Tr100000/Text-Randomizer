package io.github.tr100000.text_randomizer;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;

public final class ModConfig {
    public static final ModConfig INSTANCE = new ModConfig();

    private ModConfig() {}

    public boolean modEnabled = true;

    public boolean randomizeText = true;
    public boolean ignoreEmptyStrings = true;
    public boolean ignoreFormatSpecifiers = false;

    //? if >=1.21.11 {
    public boolean randomizeItemModels = false;
    //?}
    public boolean hideItemIds = true;
    public boolean shuffleItemGroups = false;

    public boolean useSeed = false;
    public long seed = 0;

    public static boolean should(Predicate<ModConfig> predicate) {
        return INSTANCE.modEnabled && predicate.test(INSTANCE);
    }

    public static void load() {
        try {
            if (Files.notExists(TextRandomizer.CONFIG_PATH)) {
                save();
                return;
            }

            JsonObject json = TextRandomizer.GSON.fromJson(Files.readString(TextRandomizer.CONFIG_PATH), JsonObject.class);

            INSTANCE.modEnabled = GsonHelper.getAsBoolean(json, "modEnabled", INSTANCE.modEnabled);
            INSTANCE.randomizeText = GsonHelper.getAsBoolean(json, "randomizeText", INSTANCE.randomizeText);
            INSTANCE.ignoreEmptyStrings = GsonHelper.getAsBoolean(json, "ignoreEmptyStrings", INSTANCE.ignoreEmptyStrings);
            INSTANCE.ignoreFormatSpecifiers = GsonHelper.getAsBoolean(json, "ignoreFormatSpecifiers", INSTANCE.ignoreFormatSpecifiers);

            //? if >=1.21.11 {
            INSTANCE.randomizeItemModels = GsonHelper.getAsBoolean(json, "randomizeItemModels", INSTANCE.randomizeItemModels);
            //? }
            INSTANCE.hideItemIds = GsonHelper.getAsBoolean(json, "hideItemIds", INSTANCE.hideItemIds);
            INSTANCE.shuffleItemGroups = GsonHelper.getAsBoolean(json, "shuffleItemGroups", INSTANCE.shuffleItemGroups);

            INSTANCE.useSeed = GsonHelper.getAsBoolean(json, "useSeed", INSTANCE.useSeed);
            INSTANCE.seed = GsonHelper.getAsLong(json, "seed", INSTANCE.seed);

            save();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load config file!", e);
        }
    }

    public static void save() {
        try {
            Files.deleteIfExists(TextRandomizer.CONFIG_PATH);

            JsonObject json = new JsonObject();
            json.addProperty("modEnabled", INSTANCE.modEnabled);
            json.addProperty("randomizeText", INSTANCE.randomizeText);
            json.addProperty("ignoreEmptyStrings", INSTANCE.ignoreEmptyStrings);
            json.addProperty("ignoreFormatSpecifiers", INSTANCE.ignoreFormatSpecifiers);

            //? if >=1.21.11 {
            json.addProperty("randomizeItemModels", INSTANCE.randomizeItemModels);
            //? }
            json.addProperty("hideItemIds", INSTANCE.hideItemIds);
            json.addProperty("shuffleItemGroups", INSTANCE.shuffleItemGroups);

            json.addProperty("useSeed", INSTANCE.useSeed);
            json.addProperty("seed", INSTANCE.seed);

            Files.writeString(TextRandomizer.CONFIG_PATH, TextRandomizer.GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save config file!", e);
        }
    }

    public static boolean canUseYaclConfig() {
        return ModLoaderAccess.INSTANCE.isModLoaded("yet_another_config_lib_v3");
    }
}
