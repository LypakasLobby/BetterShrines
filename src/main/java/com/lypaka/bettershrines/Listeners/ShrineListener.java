package com.lypaka.bettershrines.Listeners;

import com.google.common.collect.Lists;
import com.lypaka.bettershrines.API.ShrineActivateEvent;
import com.lypaka.bettershrines.ShrineRegistry.ConfirmationMenu;
import com.lypaka.bettershrines.ShrineRegistry.MenuButton;
import com.lypaka.bettershrines.ShrineRegistry.Shrine;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.dialogue.Choice;
import com.pixelmonmod.pixelmon.api.dialogue.Dialogue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
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

import java.util.concurrent.atomic.AtomicBoolean;

public class ShrineListener {

    @SubscribeEvent
    public void onShrine (ShrineActivateEvent event) {

        ServerPlayerEntity player = event.getPlayer();
        Shrine shrine = event.getShrine();
        if (shrine.hasConfirmationMenu()) {

            ConfirmationMenu menu = shrine.getMenu();
            Dialogue.DialogueBuilder builder = new Dialogue.DialogueBuilder();
            builder.setName(FancyText.getFormattedString(menu.getTitle()));
            String messages = FancyText.getFormattedString(String.join("\\n", menu.getText()));
            builder.setText(messages);
            builder.requireManualClose();
            for (int i = 0; i < menu.getButtons().length; i++) {

                MenuButton button = menu.getButtons()[i];
                builder.addChoice(new Choice(
                                FancyText.getFormattedString(button.getDisplayText()),
                                (choiceEvent -> {

                                    player.closeContainer();
                                    if (!button.getCommands().isEmpty()) {

                                        for (String c : button.getCommands()) {

                                            player.getServer().getCommandManager().handleCommand(player.getServer().getCommandSource(), c.replace("%player%", player.getName().getString()));

                                        }

                                    }
                                    if (!button.doesCancel()) {

                                        activateShrine(player, shrine, event);

                                    }

                                })

                        )

                );

            }
            Dialogue.setPlayerDialogueData(player, Lists.newArrayList(builder.build()), true);

        } else {

            activateShrine(player, shrine, event);

        }

    }

    private static void activateShrine (ServerPlayerEntity player, Shrine shrine, ShrineActivateEvent event) {

        if (event.getInventoryRequirement() != null) {

            event.getInventoryRequirement().removeIfNeeded();

        }
        if (event.getPokemonRequirement() != null) {

            event.getPokemonRequirement().removeIfNeeded();

        }
        if (event.getMoneyRequirement() != null) {

            event.getMoneyRequirement().payFeeIfNeeded();

        }
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
            pokemon.setPositionAndUpdate(player.getPosX(), player.getPosY(), player.getPosZ());
            player.world.addEntity(pokemon);
            WildPixelmonParticipant wpp = new WildPixelmonParticipant(pokemon);
            BattleParticipant[] wilds = new BattleParticipant[]{wpp};
            PlayerPartyStorage party = StorageProxy.getParty(player);
            PlayerParticipant pp = new PlayerParticipant(event.getPlayer(), party.getTeam(), 1);
            BattleParticipant[] players = new BattleParticipant[]{pp};
            BattleController bcb = new BattleController(wilds, players, new BattleRules());
            BattleRegistry.registerBattle(bcb);

        } else if (event.getShrine().getMode().equalsIgnoreCase("lock")) {

            PixelmonEntity pixelmon = event.getPokemon();
            if (pixelmon == null) return;
            Pokemon pokemon = pixelmon.getPokemon();
            pokemon.getPersistentData().putString("ShrinePlayer:", player.getUniqueID().toString());
            pixelmon.setPositionAndUpdate(player.getPosX(), player.getPosY(), player.getPosZ());
            player.world.addEntity(pixelmon);

        }
        for (String c : event.getCommands()) {

            server.getCommandManager().handleCommand(server.getCommandSource(), c.replace("%player%", event.getPlayer().getName().getString()));

        }

    }

}
