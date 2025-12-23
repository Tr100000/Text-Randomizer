package io.github.tr100000.text_randomizer.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.tr100000.text_randomizer.ExportUtils;
import io.github.tr100000.text_randomizer.ModConfig;
import io.github.tr100000.text_randomizer.TextRandomizer;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
    private void beforeReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) throws IOException {
        if (ModConfig.should(c -> c.exportToResourcePack)) {
            ExportUtils.deleteFolder(ExportUtils.getExportRoot());
            TextRandomizer.LOGGER.debug("Deleted old generated pack");

            JsonObject json = new JsonObject();

            JsonObject packJson = new JsonObject();
            PackFormat packVersion = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
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
                Minecraft client = Minecraft.getInstance();
                langFilterJson.addProperty("path", String.format("lang\\/%s\\.json", client.options.languageCode));
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
