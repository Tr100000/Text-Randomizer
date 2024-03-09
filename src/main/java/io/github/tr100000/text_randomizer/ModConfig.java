package io.github.tr100000.text_randomizer;

import java.nio.file.Path;

import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.SerializedName;
import org.quiltmc.config.api.serializers.TomlSerializer;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.implementor_api.ConfigEnvironment;
import org.quiltmc.config.implementor_api.ConfigFactory;

import net.fabricmc.loader.api.FabricLoader;

public final class ModConfig extends ReflectiveConfig {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(TextRandomizer.MODID + ".json");
    public static final ConfigEnvironment ENVIRONMENT = new ConfigEnvironment(FabricLoader.getInstance().getConfigDir(), TomlSerializer.INSTANCE, TomlSerializer.INSTANCE);
    public static final ModConfig INSTANCE = ConfigFactory.create(ENVIRONMENT, TextRandomizer.MODID, TextRandomizer.MODID, ModConfig.class);

    @SerializedName("mod_enabled")
    public final TrackedValue<Boolean> modEnabled = value(true);

    @SerializedName("ignore_format_specifiers")
    @Comment("Only change this if you know what you're doing!")
    public final TrackedValue<Boolean> ignoreFormatSpecifiers = value(false);

    public static boolean canUseYaclConfig() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}
