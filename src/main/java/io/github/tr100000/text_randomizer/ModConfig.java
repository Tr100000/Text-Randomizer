package io.github.tr100000.text_randomizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public final class ModConfig {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean modEnabled = true;
    public static boolean ignoreFormatSpecifiers = false;

    public static Screen generateScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
            .title(Text.literal("Text Randomizer Config"))
            .category(ConfigCategory.createBuilder()
                .name(Text.literal("Settings"))
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Mod Enabled"))
                    .binding(true, () -> modEnabled, newValue -> modEnabled = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .option(Option.<Boolean>createBuilder()
                    .name(Text.literal("Ignore Format Specifiers"))
                    .binding(false, () -> ignoreFormatSpecifiers, newValue -> ignoreFormatSpecifiers = newValue)
                    .flag(OptionFlag.ASSET_RELOAD)
                    .controller(TickBoxControllerBuilder::create)
                    .build())
                .optionIf(FabricLoader.getInstance().isDevelopmentEnvironment(), ButtonOption.createBuilder()
                    .name(Text.literal("(DEV) Reload"))
                    .action((screen, button) -> MinecraftClient.getInstance().reloadResourcesConcurrently())
                    .build())
                .build())
            .save(ModConfig::save)
            .build()
            .generateScreen(parent);
    }

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
}
