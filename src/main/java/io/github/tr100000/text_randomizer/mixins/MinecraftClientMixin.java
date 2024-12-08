package io.github.tr100000.text_randomizer.mixins;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import io.github.tr100000.text_randomizer.TextRandomizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
    private void beforeReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) throws IOException {
        if (TextRandomizer.modEnabled && TextRandomizer.exportToResourcePack) {
            TextRandomizer.deleteFolder(TextRandomizer.getExportRoot());
            TextRandomizer.LOGGER.info("Deleted old generated pack");

            JsonObject json = new JsonObject();

            JsonObject packJson = new JsonObject();
            int packFormat = SharedConstants.getGameVersion().getResourceVersion(ResourceType.CLIENT_RESOURCES);
            packJson.addProperty("pack_format", packFormat);
            packJson.addProperty("description", TextRandomizer.useSeed ? "Generated with seed " + TextRandomizer.seed : "Generated");
            json.add("pack", packJson);

            if (TextRandomizer.randomizeText) {
                JsonObject filterJson = new JsonObject();
                JsonArray filterBlocks = new JsonArray(1);
                JsonObject langFilterJson = new JsonObject();
                MinecraftClient client = MinecraftClient.getInstance();
                langFilterJson.addProperty("path", "lang/" + client.options.language + ".json");
                filterBlocks.add(langFilterJson);
                filterJson.add("block", filterBlocks);
                json.add("filter", filterJson);
            }

            TextRandomizer.exportJson(json, TextRandomizer.mcmetaPath());
        }
    }
}
