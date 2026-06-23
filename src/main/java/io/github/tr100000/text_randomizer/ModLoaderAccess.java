package io.github.tr100000.text_randomizer;

import java.nio.file.Path;

// Similar to this you can implement loader-specific functionality
// in a way that works on either mod loader.
public sealed interface ModLoaderAccess {
    ModLoaderAccess INSTANCE =
        /*? if fabric{*/new FabricLoaderAccess();
        /*?} elif neoforge *///new NeoForgeLoaderAccess();

    boolean isClient();
    boolean isServer();

    boolean isModLoaded(String id);
    Path getGameDir();

    //? if fabric {
    final class FabricLoaderAccess implements ModLoaderAccess {
        private final net.fabricmc.loader.api.FabricLoader loader = net.fabricmc.loader.api.FabricLoader.getInstance();

        @Override
        public boolean isClient() {
            return loader.getEnvironmentType().equals(net.fabricmc.api.EnvType.CLIENT);
        }

        @Override
        public boolean isServer() {
            return loader.getEnvironmentType().equals(net.fabricmc.api.EnvType.SERVER);
        }

        @Override
        public boolean isModLoaded(String id) {
            return loader.isModLoaded(id);
        }

        @Override
        public Path getGameDir() {
            return loader.getGameDir();
        }
    }
    //?} elif neoforge {
    /*final class NeoForgeLoaderAccess implements ModLoaderAccess {
        private final net.neoforged.api.distmarker.Dist dist =
            /^? if >=1.21.9 {^/net.neoforged.fml.loading.FMLEnvironment.getDist();
            /^?} else^///net.neoforged.fml.loading.FMLEnvironment.dist;
        private final net.neoforged.fml.loading.LoadingModList mods =
            /^? if >=1.21.9 {^/net.neoforged.fml.loading.FMLLoader.getCurrent().getLoadingModList();
            /^?} else^///net.neoforged.fml.loading.FMLLoader.getLoadingModList();
        private final Path gameDir =
            /^? if >=1.21.9 {^/net.neoforged.fml.loading.FMLLoader.getCurrent().getGameDir();
            /^?} else^///net.neoforged.fml.loading.FMLLoader.getGamePath();

        @Override
        public boolean isClient() {
            return dist.isClient();
        }

        @Override
        public boolean isServer() {
            return dist.isDedicatedServer();
        }

        @Override
        public boolean isModLoaded(String id) {
            return mods.getModFileById(id) != null;
        }

        @Override
        public Path getGameDir() {
            return gameDir;
        }
    }
    *///?}
}
