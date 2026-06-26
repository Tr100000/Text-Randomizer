package io.github.tr100000.text_randomizer.mixins;

//? if >=1.21.11 {

import io.github.tr100000.text_randomizer.Shuffle;
import io.github.tr100000.text_randomizer.config.ModConfig;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @ModifyVariable(method = "<init>*", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private static Map<Identifier, ClientItem> shuffleItems(Map<Identifier, ClientItem> clientInfos) {
        if (ModConfig.check(c -> c.items.randomizeModels)) {
            return Shuffle.shuffleMap(clientInfos);
        }
        else {
            return clientInfos;
        }
    }
}
//?}
