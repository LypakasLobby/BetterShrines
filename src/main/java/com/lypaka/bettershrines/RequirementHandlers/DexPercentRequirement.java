package com.lypaka.bettershrines.RequirementHandlers;

import com.pixelmonmod.pixelmon.api.pokemon.species.Pokedex;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;

public class DexPercentRequirement {

    public static boolean passes (ServerPlayerEntity player, String value) {

        PlayerPartyStorage playerParty = StorageProxy.getParty(player);
        int caught = playerParty.playerPokedex.countCaught();
        int fullDex = Pokedex.pokedexSize;
        float percent = ((float) caught / (float) fullDex) * 100;
        String[] array = value.split(" ");
        int amount = Integer.parseInt(array[1]);
        value = array[0];
        boolean passes = false;
        switch (value) {

            case ">=":
                if (percent >= amount) {

                    passes = true;

                }
                break;

            case "<=":
                if (percent <= amount) {

                    passes = true;

                }
                break;

            case ">":
                if (percent > amount) {

                    passes = true;

                }
                break;

            case "<":
                if (percent < amount) {

                    passes = true;

                }
                break;

            case "==":
            case "=":
                if (percent == amount) {

                    passes = true;

                }
                break;

        }

        return passes;

    }

}
