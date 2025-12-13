package io.github.tr100000.text_randomizer;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

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
    public boolean randomizeItemModels = false;
    public boolean hideItemIds = true;
    public boolean useSeed = false;
    public long seed = 0;
    public boolean exportToResourcePack = false;
    public boolean exportAll = false;
    public String generatedPackName = "generated";

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

            INSTANCE.modEnabled = JsonHelper.getBoolean(json, "modEnabled", INSTANCE.modEnabled);
            INSTANCE.randomizeText = JsonHelper.getBoolean(json, "randomizeText", INSTANCE.randomizeText);
            INSTANCE.ignoreEmptyStrings = JsonHelper.getBoolean(json, "ignoreEmptyStrings", INSTANCE.ignoreEmptyStrings);
            INSTANCE.ignoreFormatSpecifiers = JsonHelper.getBoolean(json, "ignoreFormatSpecifiers", INSTANCE.ignoreFormatSpecifiers);
            INSTANCE.randomizeItemModels = JsonHelper.getBoolean(json, "randomizeItemModels", INSTANCE.randomizeItemModels);
            INSTANCE.hideItemIds = JsonHelper.getBoolean(json, "hideItemIds", INSTANCE.hideItemIds);
            INSTANCE.useSeed = JsonHelper.getBoolean(json, "useSeed", INSTANCE.useSeed);
            INSTANCE.seed = JsonHelper.getLong(json, "seed", INSTANCE.seed);
            INSTANCE.exportToResourcePack = JsonHelper.getBoolean(json, "exportToResourcePack", INSTANCE.exportToResourcePack);
            INSTANCE.exportAll = JsonHelper.getBoolean(json, "exportAll", INSTANCE.exportAll);
            INSTANCE.generatedPackName = JsonHelper.getString(json, "generatedPackName", INSTANCE.generatedPackName);
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
            json.addProperty("randomizeItemModels", INSTANCE.randomizeItemModels);
            json.addProperty("hideItemIds", INSTANCE.hideItemIds);
            json.addProperty("useSeed", INSTANCE.useSeed);
            json.addProperty("seed", INSTANCE.seed);
            json.addProperty("exportToResourcePack", INSTANCE.exportToResourcePack);
            json.addProperty("exportAll", INSTANCE.exportAll);
            json.addProperty("generatedPackName", INSTANCE.generatedPackName);

            Files.writeString(TextRandomizer.CONFIG_PATH, TextRandomizer.GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save config file!", e);
        }
    }

    public static boolean canUseYaclConfig() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}
