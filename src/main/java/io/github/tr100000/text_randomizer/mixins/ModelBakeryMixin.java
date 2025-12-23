package io.github.tr100000.text_randomizer.mixins;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.tr100000.text_randomizer.ExportUtils;
import io.github.tr100000.text_randomizer.ModConfig;
import io.github.tr100000.text_randomizer.Shuffle;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ModelBakery.class)
public  abstract class ModelBakeryMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static Map<Identifier, ClientItem> shuffleItems(Map<Identifier, ClientItem> map) {
        if (ModConfig.should(c -> c.randomizeItemModels)) {
            Map<Identifier, ClientItem> newMap = Shuffle.shuffleMap(map);

            if (ModConfig.INSTANCE.exportToResourcePack) {
                export(newMap);
            }

            return newMap;
        }
        else {
            if (ModConfig.should(c -> c.exportAll)) {
                export(map);
            }

            return map;
        }
    }

    @Unique
    private static void export(Map<Identifier, ClientItem> map) {
        map.forEach((id, asset) -> {
            JsonElement json = ClientItem.CODEC.encodeStart(JsonOps.INSTANCE, asset).getOrThrow();
            ExportUtils.exportJson(json, ExportUtils.getAssetExportPath(id, "items"));
        });
    }
}
