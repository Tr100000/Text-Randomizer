package io.github.tr100000.text_randomizer.mixins;

import io.github.tr100000.text_randomizer.ModConfig;
import io.github.tr100000.text_randomizer.Shuffle;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public abstract class CreativeModeTabMixin {
    @Shadow
    private Collection<ItemStack> displayItems;

    @Shadow
    private Set<ItemStack> displayItemsSearchTab;

    @Inject(method = "getDisplayItems", at = @At("HEAD"), cancellable = true)
    private void getDisplayItems(CallbackInfoReturnable<Collection<ItemStack>> cir) {
        if (ModConfig.should(c -> c.shuffleItemGroups)) {
            cir.setReturnValue(Shuffle.shuffleList(new ArrayList<>(displayItems)));
        }
    }

    @Inject(method = "getSearchTabDisplayItems", at = @At("HEAD"), cancellable = true)
    private void getSearchTabDisplayItems(CallbackInfoReturnable<Collection<ItemStack>> cir) {
        if (ModConfig.should(c -> c.shuffleItemGroups)) {
            cir.setReturnValue(new ObjectOpenHashSet<>(Shuffle.shuffleList(new ArrayList<>(displayItemsSearchTab))));
        }
    }
}
