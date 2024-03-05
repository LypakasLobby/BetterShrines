package com.lypaka.bettershrines.Listeners;

import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BattleListeners {

    @SubscribeEvent
    public void onBattlePreStart (BattleStartedEvent.Pre event) {

        WildPixelmonParticipant wpp;
        PlayerParticipant pp;
        BattleController bc = event.getBattleController();

        if (bc.participants.get(0) instanceof WildPixelmonParticipant && bc.participants.get(1) instanceof PlayerParticipant) {

            wpp = (WildPixelmonParticipant) bc.participants.get(0);
            pp = (PlayerParticipant) bc.participants.get(1);

        } else if (bc.participants.get(0) instanceof PlayerParticipant && bc.participants.get(1) instanceof WildPixelmonParticipant) {

            wpp = (WildPixelmonParticipant) bc.participants.get(1);
            pp = (PlayerParticipant) bc.participants.get(0);

        } else {

            return;

        }

        ServerPlayerEntity player = pp.player;
        Pokemon pokemon = wpp.controlledPokemon.get(0).pokemon;
        if (pokemon.getPersistentData().contains("ShrinePlayer")) {

            String shrinePlayerUUID = pokemon.getPersistentData().getString("ShrinePlayer");
            if (!shrinePlayerUUID.equalsIgnoreCase(player.getUniqueID().toString())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText("&eThis Pokemon was not spawned for you!"), player.getUniqueID());

            }

        }

    }

    @SubscribeEvent
    public void onCaptureAttempt (CaptureEvent.StartCapture event) {

        Pokemon pokemon = event.getPokemon().getPokemon();
        ServerPlayerEntity player = event.getPlayer();
        ItemStack ball = event.getPokeBall().getBallType().getBallItem();
        ball.setCount(1);
        if (pokemon.getPersistentData().contains("ShrinePlayer")) {

            String shrinePlayerUUID = pokemon.getPersistentData().getString("ShrinePlayer");
            if (!shrinePlayerUUID.equalsIgnoreCase(player.getUniqueID().toString())) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText("&eThis Pokemon was not spawned for you!"), player.getUniqueID());
                player.addItemStackToInventory(ball);

            }

        }

    }

}
