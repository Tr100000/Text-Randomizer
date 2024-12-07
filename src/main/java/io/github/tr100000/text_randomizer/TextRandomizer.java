package io.github.tr100000.text_randomizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

public class TextRandomizer implements ClientModInitializer {
    public static final String MODID = "text_randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Text Randomizer");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");
    public static final Path EXPORT_PATH = FabricLoader.getInstance().getGameDir().resolve("export/lang.json");

    public static boolean randomizeText = true;
    public static boolean ignoreFormatSpecifiers = false;
    public static boolean exportLanguage = false;
    public static boolean randomizeItemModels = false;
    public static boolean useSeed = false;
    public static long seed = 0;

    @Override
    public void onInitializeClient() {
        loadConfig();
    }


    public static void loadConfig() {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                saveConfig();
                return;
            }

            JsonObject json = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);
            randomizeText = JsonHelper.getBoolean(json, "modEnabled", randomizeText);
            ignoreFormatSpecifiers = JsonHelper.getBoolean(json, "ignoreFormatSpecifiers", ignoreFormatSpecifiers);
            exportLanguage = JsonHelper.getBoolean(json, "exportLanguage", exportLanguage);
            randomizeItemModels = JsonHelper.getBoolean(json, "randomizeItemModels", randomizeItemModels);
            useSeed = JsonHelper.getBoolean(json, "useSeed", useSeed);
            seed = JsonHelper.getLong(json, "seed", seed);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load config file!", e);
        }
    }

    public static void saveConfig() {
        try {
            Files.deleteIfExists(CONFIG_PATH);

            JsonObject json = new JsonObject();
            json.addProperty("randomizeText", randomizeText);
            json.addProperty("ignoreFormatSpecifiers", ignoreFormatSpecifiers);
            json.addProperty("exportLanguage", exportLanguage);
            json.addProperty("randomizeItemModels", randomizeItemModels);
            json.addProperty("useSeed", useSeed);
            json.addProperty("seed", seed);

            Files.writeString(CONFIG_PATH, GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save config file!", e);
        }
    }

    public static boolean canUseYaclConfig() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }

    public static void trySaveLanguage(Map<String, String> translations) {
        if (!exportLanguage) {
            return;
        }
        try {
            JsonObject json = new JsonObject();
            translations.forEach(json::addProperty);
            Files.deleteIfExists(EXPORT_PATH);
            Files.writeString(EXPORT_PATH, GSON.toJson(json));
        }
        catch (IOException e) {
            LOGGER.warn("Failed to export language file!", e);
        }
    }
}
