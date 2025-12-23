package io.github.tr100000.text_randomizer.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.tr100000.text_randomizer.ModConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @WrapOperation(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/Identifier;toString()Ljava/lang/String;"))
    private String replaceItemId(Identifier value, Operation<String> original) {
        return ModConfig.should(c -> (c.randomizeText || c.randomizeItemModels) && c.hideItemIds) ? "item id goes here" : original.call(value);
    }
}
