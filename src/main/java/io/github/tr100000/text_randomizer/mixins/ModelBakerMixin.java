package io.github.tr100000.text_randomizer.mixins;

import java.util.Map;

import io.github.tr100000.text_randomizer.Shuffle;
import io.github.tr100000.text_randomizer.TextRandomizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.util.Identifier;

@Mixin(ModelBaker.class)
public class ModelBakerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1)
    private static Map<Identifier, ItemAsset> shuffleItems(Map<Identifier, ItemAsset> map) {
        if (TextRandomizer.modEnabled && TextRandomizer.randomizeItemModels) {
            Map<Identifier, ItemAsset> newMap = Shuffle.shuffleMap(map);

            if (TextRandomizer.exportToResourcePack) {
                newMap.forEach((id, asset) -> {
                    JsonElement json = ItemAsset.CODEC.encodeStart(JsonOps.INSTANCE, asset).getOrThrow();
                    TextRandomizer.exportJson(json, TextRandomizer.getAssetExportPath(id, "items"));
                });
            }

            return newMap;
        }
        else {
            return map;
        }
    }
}
