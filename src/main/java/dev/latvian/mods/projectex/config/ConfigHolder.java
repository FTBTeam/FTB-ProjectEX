/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.latvian.mods.projectex.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {
    static ClientConfig client;
    static ServerConfig server;
    private static ForgeConfigSpec configClientSpec;
    private static ForgeConfigSpec configServerSpec;

    public static void init() {
        final Pair<ClientConfig, ForgeConfigSpec> clientSpec = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        client = clientSpec.getLeft();
        configClientSpec = clientSpec.getRight();

        final Pair<ServerConfig, ForgeConfigSpec> serverSpec = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        server = serverSpec.getLeft();
        configServerSpec = serverSpec.getRight();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHolder.configClientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHolder.configServerSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigHolder::onConfigChanged);
    }

    private static void onConfigChanged(final ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigHolder.configClientSpec) {
            refreshClient();
        } else if (config.getSpec() == ConfigHolder.configServerSpec) {
            refreshServer();
        }
    }

    private static void refreshServer() {
    }

    private static void refreshClient() {
    }
}
