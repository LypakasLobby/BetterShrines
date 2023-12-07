package com.lypaka.bettershrines.Listeners;

import com.lypaka.bettershrines.BetterShrines;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = BetterShrines.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new BlockInteractListener());
        MinecraftForge.EVENT_BUS.register(new EntityInteractListener());
        MinecraftForge.EVENT_BUS.register(new ShrineListener());

    }

}
