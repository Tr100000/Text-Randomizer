package io.github.tr100000.text_randomizer;

import static io.github.tr100000.text_randomizer.TextRandomizer.GSON;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

public final class ModConfig {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");

    public static boolean modEnabled = true;
    public static boolean ignoreFormatSpecifiers = false;

    public static void load() {
        try {
            if (Files.notExists(PATH)) {
                save();
                return;
            }

            JsonObject json = GSON.fromJson(Files.readString(PATH), JsonObject.class);
            modEnabled = JsonHelper.getBoolean(json, "modEnabled", modEnabled);
            ignoreFormatSpecifiers = JsonHelper.getBoolean(json, "ignoreFormatSpecifiers", ignoreFormatSpecifiers);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load config file!", e);
        }
    }

    public static void save() {
        try {
            Files.deleteIfExists(PATH);

            JsonObject json = new JsonObject();
            json.addProperty("modEnabled", modEnabled);
            json.addProperty("ignoreFormatSpecifiers", ignoreFormatSpecifiers);

            Files.writeString(PATH, GSON.toJson(json));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save config file!", e);
        }
    }

    public static boolean canUseYaclConfig() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}
