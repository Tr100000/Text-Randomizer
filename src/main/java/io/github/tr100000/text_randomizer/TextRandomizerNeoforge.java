package io.github.tr100000.text_randomizer;

//? if neoforge {
/*import io.github.tr100000.text_randomizer.config.ModConfig;
import io.github.tr100000.text_randomizer.integration.ModYaclConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = "text_randomizer", dist = Dist.CLIENT)
public class TextRandomizerNeoforge {
    public TextRandomizerNeoforge(IEventBus modEventBus, ModContainer modContainer) {
        TextRandomizer.init();

        if (ModConfig.canUseYaclConfig()) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) -> ModYaclConfig.generateScreen(parent)
            );
        }
    }
}
*///?}
