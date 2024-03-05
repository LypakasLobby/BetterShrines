package com.lypaka.bettershrines.RequirementHandlers;

import com.lypaka.bettershrines.BetterShrines;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.stats.Moveset;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonRequirement {

    private final ServerPlayerEntity player;
    private final Map<String, Map<String, String>> pokemonMap;
    private final List<Integer> sacrificeList;

    public PokemonRequirement (ServerPlayerEntity player, Map<String, Map<String, String>> pokemonMap) {

        this.player = player;
        this.pokemonMap = pokemonMap;
        this.sacrificeList = new ArrayList<>();

    }

    public boolean passes() {

        PlayerPartyStorage party = StorageProxy.getParty(this.player);
        Map<Integer, Boolean> passMap = new HashMap<>();
        Map<String, Boolean> presentInParty = new HashMap<>();
        if (!this.pokemonMap.isEmpty()) {

            for (Map.Entry<String, Map<String, String>> entry : this.pokemonMap.entrySet()) {

                presentInParty.put(entry.getKey(), false);

            }

        }
        for (int i = 0; i < 6; i++) {

            Pokemon pokemon = party.get(i);
            if (pokemon != null) {

                if (this.pokemonMap.containsKey(pokemon.getLocalizedName())) {

                    passMap.put(i, true);
                    presentInParty.entrySet().forEach(e -> {

                        if (e.getKey().equalsIgnoreCase(pokemon.getLocalizedName())) {

                            e.setValue(true);

                        }

                    });
                    Map<String, String> map = this.pokemonMap.get(pokemon.getLocalizedName());
                    for (Map.Entry<String, String> specEntry : map.entrySet()) {

                        String spec = specEntry.getKey();
                        String value = specEntry.getValue();

                        switch (spec.toLowerCase()) {

                            case "ability":
                                if (passMap.get(i)) {

                                    if (!pokemon.getAbility().getLocalizedName().equalsIgnoreCase(value)) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "ball":
                            case "caughtball":
                            case "pokeball":
                                if (passMap.get(i)) {

                                    if (!pokemon.getBall().getBallItem().getItem().getRegistryName().toString().equalsIgnoreCase(value)) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "dynamax-level":
                                if (passMap.get(i)) {

                                    int currentLevel = pokemon.getDynamaxLevel();
                                    int needed = Integer.parseInt(value);
                                    if (currentLevel < needed) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "form":
                                if (passMap.get(i)) {

                                    String current = pokemon.getForm().getLocalizedName();
                                    if (!(current.equalsIgnoreCase(value))) {

                                        if (!value.equalsIgnoreCase("any")) {

                                            passMap.put(i, false);

                                        }

                                    }

                                }
                                break;

                            case "gender":
                                if (passMap.get(i)) {

                                    Gender pokeGender = pokemon.getGender();
                                    Gender neededGender = Gender.valueOf(value);
                                    if (pokeGender != neededGender) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "growth":
                                if (passMap.get(i)) {

                                    EnumGrowth pokeGrowth = pokemon.getGrowth();
                                    EnumGrowth neededGrowth = EnumGrowth.valueOf(value);
                                    if (pokeGrowth != neededGrowth) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "happiness":
                                if (passMap.get(i)) {

                                    int pokeHappiness = pokemon.getFriendship();
                                    int neededHappiness = Integer.parseInt(value);
                                    if (pokeHappiness < neededHappiness) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "held-item":
                                if (passMap.get(i)) {

                                    if (!pokemon.getHeldItem().toString().contains("1xtile.air")) { // I have no idea why the fuck I'm checking for it this way, but whatever

                                        ItemStack pokemonItem = pokemon.getHeldItem();
                                        String id = pokemonItem.getItem().getRegistryName().toString();
                                        if (!id.equalsIgnoreCase(value) || value.equalsIgnoreCase("none")) {

                                            passMap.put(i, false);

                                        }

                                    }

                                }
                                break;

                            case "level":
                                if (passMap.get(i)) {

                                    int pokeLevel = pokemon.getPokemonLevel();
                                    int neededLevel = Integer.parseInt(value);

                                    if (neededLevel != pokeLevel) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "move-1":
                                if (passMap.get(i)) {

                                    Moveset moves = pokemon.getMoveset();
                                    if (moves.get(0) != null) {

                                        Attack attack1 = moves.get(0);
                                        if (!attack1.getMove().getLocalizedName().replace(" ", "").equalsIgnoreCase(value.replace(" ", ""))) {

                                            passMap.put(i, false);

                                        }

                                    } else {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "move-2":
                                if (passMap.get(i)) {

                                    Moveset moves = pokemon.getMoveset();
                                    if (moves.get(1) != null) {

                                        Attack attack2 = moves.get(1);
                                        if (!attack2.getMove().getLocalizedName().replace(" ", "").equalsIgnoreCase(value.replace(" ", ""))) {

                                            passMap.put(i, false);

                                        }

                                    } else {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "move-3":
                                if (passMap.get(i)) {

                                    Moveset moves = pokemon.getMoveset();
                                    if (moves.get(2) != null) {

                                        Attack attack3 = moves.get(2);
                                        if (!attack3.getMove().getLocalizedName().replace(" ", "").equalsIgnoreCase(value.replace(" ", ""))) {

                                            passMap.put(i, false);

                                        }

                                    } else {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "move-4":
                                if (passMap.get(i)) {

                                    Moveset moves = pokemon.getMoveset();
                                    if (moves.get(3) != null) {

                                        Attack attack4 = moves.get(3);
                                        if (!attack4.getMove().getLocalizedName().replace(" ", "").equalsIgnoreCase(value.replace(" ", ""))) {

                                            passMap.put(i, false);

                                        }

                                    } else {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "nature":
                                if (passMap.get(i)) {

                                    Nature pokemonNature = pokemon.getNature();
                                    Nature neededNature = Nature.natureFromString(value);
                                    if (pokemonNature != neededNature) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "ot":
                                if (passMap.get(i)) {

                                    if (pokemon.getOriginalTrainer() == null) {

                                        passMap.put(i, false);

                                    } else if (!pokemon.getOriginalTrainer().equalsIgnoreCase(this.player.getName().getString())) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "shiny":
                                if (passMap.get(i)) {

                                    boolean needed = Boolean.parseBoolean(value);
                                    if (!needed && pokemon.isShiny()) {

                                        passMap.put(i, false);

                                    } else if (needed && !pokemon.isShiny()) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                            case "slot":
                                if (passMap.get(i)) {

                                    int needed = Integer.parseInt(value);
                                    if (i != needed) {

                                        passMap.put(i, false);

                                    }

                                }
                                break;

                        }

                    }

                    if (passMap.get(i)) {

                        if (map.containsKey("Sacrifice")) {

                            boolean sacrifice = Boolean.parseBoolean(map.get("Sacrifice"));
                            if (sacrifice) {

                                this.sacrificeList.add(i);

                            }

                        }

                    }

                }

            }

        }

        boolean passes = true;
        for (Map.Entry<String, Boolean> entry : presentInParty.entrySet()) {

            if (!entry.getValue()) {

                passes = false;
                break;

            }

        }
        for (Map.Entry<Integer, Boolean> entry : passMap.entrySet()) {

            if (!entry.getValue()) {

                passes = false;
                break;

            }

        }

        return passes;

    }

    public void removeIfNeeded() {

        if (this.sacrificeList.size() == 6) {

            BetterShrines.logger.error("Cannot remove 6 Pokemon from the player! This would cause them to have an empty party and Pixelmon doesn't like that.");
            return;

        }

        PlayerPartyStorage party = StorageProxy.getParty(this.player);
        for (int i : this.sacrificeList) {

            party.set(i, null);

        }

    }

}
