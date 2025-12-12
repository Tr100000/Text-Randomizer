package io.github.tr100000.text_randomizer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class ExportUtils {
	private ExportUtils() {}

	public static void trySaveLanguage(Map<String, String> translations) {
		if (!ModConfig.INSTANCE.exportToResourcePack) {
			return;
		}
		JsonObject json = new JsonObject();
		translations.forEach(json::addProperty);
		MinecraftClient client = MinecraftClient.getInstance();
		exportJson(json, getAssetExportPath(Identifier.ofVanilla(client.options.language), "lang"));
	}

	public static Path getExportRoot() {
		return FabricLoader.getInstance().getGameDir().resolve("resourcepacks").resolve(ModConfig.INSTANCE.generatedPackName);
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
			Files.writeString(path, TextRandomizer.GSON.toJson(json));
		}
		catch (IOException e) {
			TextRandomizer.LOGGER.warn("Failed to export file to {}", path, e);
		}
	}

	public static void deleteFolder(Path folder) throws IOException {
		if (!Files.exists(folder)) return;
		List<Path> paths = Files.list(folder).toList();
		for (Path path : paths) {
			if (Files.isDirectory(path)) {
				deleteFolder(path);
			} else {
				Files.delete(path);
			}
		}
		Files.delete(folder);
	}
}
