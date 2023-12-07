package com.lypaka.bettershrines.RequirementHandlers;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class MoonPhaseRequirement {

    public static boolean pass (ServerPlayerEntity player, List<Integer> phases) {

        int current = player.world.getMoonPhase();
        for (int phase : phases) {

            if (current == phase) return true;

        }

        return false;

    }

}
