package io.github.tr100000.text_randomizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public final class TextRandomizer {
    private TextRandomizer() {}

    public static final String MODID = "text_randomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger("Text Randomizer");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String VERSION = /*$ mod_version*/ "0.2.0";
    public static final String MINECRAFT = /*$ minecraft*/ "26.2";

    public static final Path CONFIG_PATH = ModLoaderAccess.INSTANCE.getGameDir().resolve(TextRandomizer.MODID + ".json");

    @ApiStatus.Internal
    public static void init() {
        ModConfig.load();
    }
}
