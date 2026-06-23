package io.github.tr100000.text_randomizer;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;

public class TextRandomizerFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TextRandomizer.init();
    }
}
//?}
