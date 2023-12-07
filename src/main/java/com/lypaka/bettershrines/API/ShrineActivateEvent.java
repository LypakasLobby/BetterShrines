package com.lypaka.bettershrines.API;

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

    public ShrineActivateEvent (ServerPlayerEntity player, Shrine shrine, PixelmonEntity pokemon, ArrayList<String> commands) {

        this.player = player;
        this.shrine = shrine;
        this.pokemon = pokemon;
        this.commands = commands;

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

}
