package io.github.tr100000.text_randomizer.mixins;

import io.github.tr100000.text_randomizer.TextRandomizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;toString()Ljava/lang/String;"))
    private String replaceItemId(Identifier value, Operation<String> original) {
        return TextRandomizer.modEnabled && TextRandomizer.hideItemIds ? "???" : original.call(value);
    }
}
