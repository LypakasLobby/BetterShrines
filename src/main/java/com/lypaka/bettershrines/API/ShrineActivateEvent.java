package com.lypaka.bettershrines.API;

import com.lypaka.bettershrines.RequirementHandlers.InventoryRequirement;
import com.lypaka.bettershrines.RequirementHandlers.MoneyRequirement;
import com.lypaka.bettershrines.RequirementHandlers.PokemonRequirement;
import com.lypaka.bettershrines.ShrineRegistry.Shrine;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;

@Cancelable
public class ShrineActivateEvent extends Event {

    private final ServerPlayerEntity player;
    private final Shrine shrine;
    private PixelmonEntity pokemon;
    private final ArrayList<String> commands;
    private final InventoryRequirement inventoryRequirement;
    private final PokemonRequirement pokemonRequirement;
    private final MoneyRequirement moneyRequirement;

    public ShrineActivateEvent (ServerPlayerEntity player, Shrine shrine, PixelmonEntity pokemon, ArrayList<String> commands, InventoryRequirement inventoryRequirement, PokemonRequirement pokemonRequirement, MoneyRequirement moneyRequirement) {

        this.player = player;
        this.shrine = shrine;
        this.pokemon = pokemon;
        this.commands = commands;
        this.inventoryRequirement = inventoryRequirement;
        this.pokemonRequirement = pokemonRequirement;
        this.moneyRequirement = moneyRequirement;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Shrine getShrine() {

        return this.shrine;

    }

    public PixelmonEntity getPokemon() {

        return this.pokemon;

    }

    public void setPokemon (PixelmonEntity pokemon) {

        this.pokemon = pokemon;

    }

    public ArrayList<String> getCommands() {

        return this.commands;

    }

    public InventoryRequirement getInventoryRequirement() {

        return this.inventoryRequirement;

    }

    public PokemonRequirement getPokemonRequirement() {

        return this.pokemonRequirement;

    }

    public MoneyRequirement getMoneyRequirement() {

        return this.moneyRequirement;

    }

}
