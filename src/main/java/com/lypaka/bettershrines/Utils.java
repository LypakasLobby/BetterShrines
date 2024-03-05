package com.lypaka.bettershrines;

import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static PixelmonEntity buildPokemonFromCommand (String command) {

        PixelmonEntity spawnedPokemon = null;
        String[] args = command.split(" ");
        String pokemonName = null;
        List<String> specs = new ArrayList<>();
        for (Species species : PixelmonSpecies.getAll()) {

            for (String a : args) {

                if (a.equalsIgnoreCase(species.getName())) {

                    pokemonName = species.getName();
                    break;

                }

            }
            if (pokemonName != null) break;

        }

        for (String a : args) {

            if (a.contains(":")) {

                specs.add(a);

            }

        }

        if (pokemonName != null) {

            spawnedPokemon = PokemonBuilder.builder().species(pokemonName).build().getOrCreatePixelmon();
            boolean hasIVs = false;
            boolean hasGender = false;
            if (!specs.isEmpty()) {

                for (String pokemonSpec : specs) {

                    String[] specSplit = pokemonSpec.split(":");
                    String key = specSplit[0].toLowerCase();
                    String value = specSplit[1];

                    switch (key) {

                        case "ability":
                        case "ab":
                            spawnedPokemon.getPokemon().setAbility(AbilityRegistry.getAbility(value));
                            break;

                        case "form":
                        case "f":

                        case "special":
                        case "sp":
                            spawnedPokemon.setForm(value);
                            break;

                        case "growth":
                        case "gr":
                            spawnedPokemon.getPokemon().setGrowth(EnumGrowth.getGrowthFromString(value));
                            break;

                        case "shiny":
                        case "s":
                            spawnedPokemon.getPokemon().setShiny(Boolean.parseBoolean(value));
                            break;

                        case "boss":
                        case "b":
                            spawnedPokemon.setBossTier(BossTierRegistry.getBossTierUnsafe(value));
                            break;

                        case "level":
                        case "lvl":
                            spawnedPokemon.getLvl().setLevel(Integer.parseInt(value));
                            break;

                        case "levelrange":
                            String[] lvlSplit = value.split("-");
                            int min = Integer.parseInt(lvlSplit[0]);
                            int max = Integer.parseInt(lvlSplit[1]);
                            spawnedPokemon.getLvl().setLevel(RandomHelper.getRandomNumberBetween(min, max));
                            break;

                        case "ivs":
                            hasIVs = true;
                            String[] s = value.split(", ");
                            int[] iv = new int[6];
                            for (int i = 0; i < 6; i++) {

                                iv[i] = Integer.parseInt(s[i]);

                            }
                            spawnedPokemon.getPokemon().getIVs().fillFromArray(iv);
                            break;

                        case "gmax":
                            spawnedPokemon.getPokemon().setGigantamaxFactor(Boolean.parseBoolean(value));
                            break;

                        case "gender":
                        case "g":
                            hasGender = true;
                            spawnedPokemon.getPokemon().setGender(Gender.getGender(value));
                            break;

                        case "nature":
                        case "n":
                            spawnedPokemon.getPokemon().setNature(Nature.natureFromString(value));
                            break;

                        case "pseudonature":
                            spawnedPokemon.getPokemon().setMintNature(Nature.natureFromString(value));
                            break;

                        case "ct":
                        case "customtexture":
                            spawnedPokemon.getPokemon().setPalette(value);
                            break;

                        case "evs":
                            String[] evSplit = value.split("/");
                            int[] evArray = new int[6];
                            if (evSplit.length == 6) {

                                for (int i = 0; i < 6; i++) {

                                    evArray[i] = Integer.parseInt(evSplit[0]);

                                }

                                spawnedPokemon.getPokemon().getEVs().fillFromArray(evArray);

                            } else {

                                BetterShrines.logger.warn("Detected EVs spec with less than 6 values, not applying!");

                            }
                            break;

                    }

                }

                if (!hasIVs) {

                    int[] ivs = new int[6];
                    int perfectCount = 0;
                    for (int i = 0; i < 6; i++) {

                        int value = RandomHelper.getRandomNumberBetween(1, 31);
                        if (value == 31) perfectCount++;

                    }
                    if (PixelmonSpecies.isLegendary(spawnedPokemon.getSpecies()) || PixelmonSpecies.isMythical(spawnedPokemon.getSpecies()) || PixelmonSpecies.isUltraBeast(spawnedPokemon.getSpecies())) {

                        if (perfectCount < 3) {

                            List<Integer> notPerfectIVSlots = new ArrayList<>();
                            for (int i = 0; i < 6; i++) {

                                if (ivs[i] != 31) {

                                    notPerfectIVSlots.add(i);

                                }

                            }

                            for (int i = perfectCount; i <= 3; i++) {

                                int slot = RandomHelper.getRandomElementFromList(notPerfectIVSlots);
                                ivs[slot] = 31;
                                notPerfectIVSlots.removeIf(e -> e == slot);

                            }

                        }

                    }
                    spawnedPokemon.getPokemon().getIVs().fillFromArray(ivs);

                }
                if (!hasGender) {

                    spawnedPokemon.getPokemon().setGender(Gender.getRandomGender(spawnedPokemon.getPokemon().getForm()));

                }
                spawnedPokemon.update();

            }

        }

        return spawnedPokemon;

    }

}
