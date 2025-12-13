package io.github.tr100000.text_randomizer.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.tr100000.text_randomizer.ExportUtils;
import io.github.tr100000.text_randomizer.ModConfig;
import io.github.tr100000.text_randomizer.TextRandomizer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.PackVersion;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
    private void beforeReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) throws IOException {
        if (ModConfig.should(c -> c.exportToResourcePack)) {
            ExportUtils.deleteFolder(ExportUtils.getExportRoot());
            TextRandomizer.LOGGER.debug("Deleted old generated pack");

            JsonObject json = new JsonObject();

            JsonObject packJson = new JsonObject();
            PackVersion packVersion = SharedConstants.getGameVersion().packVersion(ResourceType.CLIENT_RESOURCES);
            JsonArray packVersionArray = new JsonArray();
            packVersionArray.add(packVersion.major());
            packVersionArray.add(packVersion.minor());
            packJson.add("min_format", packVersionArray);
            packJson.add("max_format", packVersionArray);
            packJson.addProperty("description", ModConfig.INSTANCE.useSeed ? "Generated with seed " + ModConfig.INSTANCE.seed : "Generated");
            json.add("pack", packJson);

            JsonObject filterJson = new JsonObject();
            JsonArray filterBlocks = new JsonArray();

            if (ModConfig.INSTANCE.randomizeText || ModConfig.INSTANCE.exportAll) {
                JsonObject langFilterJson = new JsonObject();
                MinecraftClient client = MinecraftClient.getInstance();
                langFilterJson.addProperty("path", String.format("lang\\/%s\\.json", client.options.language));
                filterBlocks.add(langFilterJson);
            }

            if (ModConfig.INSTANCE.randomizeItemModels || ModConfig.INSTANCE.exportAll) {
                JsonObject itemModelFilterJson = new JsonObject();
                itemModelFilterJson.addProperty("path", "items\\/.*");
                filterBlocks.add(itemModelFilterJson);
            }

            filterJson.add("block", filterBlocks);
            json.add("filter", filterJson);

            ExportUtils.exportJson(json, ExportUtils.mcmetaPath());
        }
    }
}
