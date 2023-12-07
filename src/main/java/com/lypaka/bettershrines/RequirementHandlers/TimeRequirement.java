package com.lypaka.bettershrines.RequirementHandlers;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class TimeRequirement {

    public static boolean passes (ServerPlayerEntity player, List<String> times) {

        List<WorldTime> worldTime = WorldTime.getCurrent(player.world);
        List<String> worldTimeNames = new ArrayList<>();
        for (WorldTime time : worldTime) {

            worldTimeNames.add(time.name());

        }

        for (String t : times) {

            for (String w : worldTimeNames) {

                if (t.equalsIgnoreCase(w)) {

                    return true;

                }

            }

        }

        return false;

    }

}
