package com.lypaka.bettershrines.RequirementHandlers;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class WeatherRequirement {

    public static boolean passes (ServerPlayerEntity player, List<String> weathers) {

        String weather;
        if (player.world.isRaining()) {

            weather = "rain";

        } else if (player.world.isThundering()) {

            weather = "storm";

        } else {

            weather = "clear";

        }

        for (String w : weathers) {

            if (w.equalsIgnoreCase(weather)) {

                return true;

            }

        }

        return false;

    }

}
