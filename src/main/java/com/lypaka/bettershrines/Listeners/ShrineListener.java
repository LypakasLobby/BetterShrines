package com.lypaka.bettershrines.Listeners;

import com.lypaka.bettershrines.API.ShrineActivateEvent;
import com.lypaka.bettershrines.ShrineRegistry.Shrine;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ShrineListener {

    @SubscribeEvent
    public void onShrine (ShrineActivateEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        Shrine shrine = event.getShrine();
        MinecraftServer server = player.world.getServer();
        if (shrine.getMaxActivationAmount() != 0) {

            int amount = shrine.getMaxActivationAmount();
            String perm = shrine.getName().replace(".conf", "").toLowerCase();
            if (amount == 1) {

                if (!PermissionHandler.hasPermission(player, "bs." + perm + ".1")) {

                    server.getCommandManager().handleCommand(server.getCommandSource(), "lutils permission add " + player.getName().getString() + " bs." + perm + ".1");

                }

            } else {

                if (!PermissionHandler.hasPermission(player, "bs." + perm + ".1")) {

                    server.getCommandManager().handleCommand(server.getCommandSource(), "lutils permission add " + player.getName().getString() + " bs." + perm + ".1");


                } else {

                    for (int i = 2; i <= amount; i++) {

                        int next = i + 1;
                        if (!PermissionHandler.hasPermission(player, "bs." + perm + "." + i)) {

                            server.getCommandManager().handleCommand(server.getCommandSource(), "lutils permission add " + player.getName().getString() + " bs." + perm + "." + i);


                        } else {

                            if (i != amount) {

                                server.getCommandManager().handleCommand(server.getCommandSource(), "lutils permission add " + player.getName().getString() + " bs." + perm + "." + next);

                            }

                        }

                    }

                }

            }

        }
        if (event.getShrine().getMode().equalsIgnoreCase("auto-start")) {

            PixelmonEntity pokemon = event.getPokemon();
            if (pokemon == null) return;
            WildPixelmonParticipant wpp = new WildPixelmonParticipant(pokemon);
            BattleParticipant[] wilds = new BattleParticipant[]{wpp};
            PlayerPartyStorage party = StorageProxy.getParty(player);
            PlayerParticipant pp = new PlayerParticipant(event.getPlayer(), party.getTeam(), 1);
            BattleParticipant[] players = new BattleParticipant[]{pp};
            BattleController bcb = new BattleController(wilds, players, new BattleRules());
            BattleRegistry.registerBattle(bcb);

        }
        for (String c : event.getCommands()) {

            server.getCommandManager().handleCommand(server.getCommandSource(), c.replace("%player%", event.getPlayer().getName().getString()));

        }

    }

}
