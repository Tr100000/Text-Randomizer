package io.github.tr100000.text_randomizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class TextRandomizer implements ClientModInitializer {
    public static final String MODID = "text_randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Text Randomizer");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");

    public static boolean modEnabled = true;
    public static boolean randomizeText = true;
    public static boolean ignoreFormatSpecifiers = false;
    public static boolean randomizeItemModels = false;
    public static boolean hideItemIds = false;
    public static boolean useSeed = false;
    public static long seed = 0;
    public static boolean exportToResourcePack = false;
    public static String generatedPackName = "generated";

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
            modEnabled = JsonHelper.getBoolean(json, "modEnabled", modEnabled);
            randomizeText = JsonHelper.getBoolean(json, "randomizeText", randomizeText);
            ignoreFormatSpecifiers = JsonHelper.getBoolean(json, "ignoreFormatSpecifiers", ignoreFormatSpecifiers);
            randomizeItemModels = JsonHelper.getBoolean(json, "randomizeItemModels", randomizeItemModels);
            hideItemIds = JsonHelper.getBoolean(json, "hideItemIds", hideItemIds);
            useSeed = JsonHelper.getBoolean(json, "useSeed", useSeed);
            seed = JsonHelper.getLong(json, "seed", seed);
            exportToResourcePack = JsonHelper.getBoolean(json, "exportToResourcePack", exportToResourcePack);
            generatedPackName = JsonHelper.getString(json, "generatedPackName", generatedPackName);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load config file!", e);
        }
    }

    public static void saveConfig() {
        try {
            Files.deleteIfExists(CONFIG_PATH);

            JsonObject json = new JsonObject();
            json.addProperty("modEnabled", modEnabled);
            json.addProperty("randomizeText", randomizeText);
            json.addProperty("ignoreFormatSpecifiers", ignoreFormatSpecifiers);
            json.addProperty("randomizeItemModels", randomizeItemModels);
            json.addProperty("hideItemIds", hideItemIds);
            json.addProperty("useSeed", useSeed);
            json.addProperty("seed", seed);
            json.addProperty("exportToResourcePack", exportToResourcePack);
            json.addProperty("generatedPackName", generatedPackName);

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
        if (!exportToResourcePack) {
            return;
        }
        JsonObject json = new JsonObject();
        translations.forEach(json::addProperty);
        MinecraftClient client = MinecraftClient.getInstance();
        exportJson(json, getAssetExportPath(Identifier.ofVanilla(client.options.language), "lang"));
    }

    public static Path getExportRoot() {
        return FabricLoader.getInstance().getGameDir().resolve("resourcepacks").resolve(generatedPackName);
    }

    public static Path getAssetExportFolder(String namespace, String directory) {
        return getExportRoot().resolve("assets").resolve(namespace).resolve(directory);
    }

    public static Path getAssetExportPath(Identifier id, String directory) {
        return getAssetExportFolder(id.getNamespace(), directory).resolve(id.getPath() + ".json");
    }

    public static Path mcmetaPath() {
        return getExportRoot().resolve("pack.mcmeta");
    }

    public static void exportJson(JsonElement json, Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(json));
        }
        catch (IOException e) {
            LOGGER.warn("Failed to export file to {}", path, e);
        }
    }

    public static boolean deleteFolder(Path folder) throws IOException {
        if (!Files.exists(folder)) return false;
        List<Path> paths = Files.list(folder).toList();
        if (paths != null) {
            for (Path path : paths) {
                if (Files.isDirectory(path)) {
                    deleteFolder(path);
                }
                else {
                    Files.delete(path);
                }
            }
        }
        Files.delete(folder);
        return true;
    }
}
