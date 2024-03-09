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

public class TextRandomizer implements ClientModInitializer {
    public static final String MODID = "text_randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Text Randomizer");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {
        ModConfig.load();
    }

    public static void saveLanguage(Map<String, String> translations) {
        try {
            JsonObject json = new JsonObject();
            translations.forEach(json::addProperty);
            Path path = FabricLoader.getInstance().getGameDir().resolve("export/lang.json");
            Files.deleteIfExists(path);
            Files.writeString(path, GSON.toJson(json));
        }
        catch (IOException e) {}
    }
}
