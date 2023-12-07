package com.lypaka.bettershrines.Commands;

import com.lypaka.bettershrines.BetterShrines;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterShrines.MOD_ID)
public class BetterShrinesCommand {

    public static List<String> ALIASES = Arrays.asList("bettershrines", "bshrines", "shrines");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
