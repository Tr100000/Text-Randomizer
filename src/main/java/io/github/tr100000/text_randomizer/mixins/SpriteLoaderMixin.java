package io.github.tr100000.text_randomizer.mixins;

import io.github.tr100000.text_randomizer.Shuffle;
import io.github.tr100000.text_randomizer.config.ModConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(SpriteLoader.class)
public abstract class SpriteLoaderMixin {
    @ModifyVariable(method = "stitch", at = @At("HEAD"), argsOnly = true)
    private List<SpriteContents> messWithStitch(List<SpriteContents> sprites) {
        if (!ModConfig.check(c -> c.textures.shuffleAtlasTextures)) return sprites;

        Map<SpriteContents, Identifier> spriteIds = sprites.stream()
                .collect(Collectors.toMap(
                        entry -> entry,
                        Stitcher.Entry::name
                ));

        Map<SpriteContents, Identifier> shuffled = Shuffle.shuffleMap(spriteIds);
        List<SpriteContents> shuffledList = new ObjectArrayList<>();

        for (Map.Entry<SpriteContents, Identifier> entry : shuffled.entrySet()) {
            entry.getKey().name = entry.getValue();
            shuffledList.add(entry.getKey());
        }

        return shuffledList;
    }
}
