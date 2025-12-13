package io.github.tr100000.text_randomizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TextRandomizer implements ClientModInitializer {
    public static final String MODID = "text_randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Text Randomizer");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");

    @Override
    public void onInitializeClient() {
        ModConfig.load();
    }
}
