package com.lypaka.bettershrines.RequirementHandlers;

import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Map;

public class PermissionRequirement {

    public static boolean passes (ServerPlayerEntity player, Map<String, Boolean> permMap) {

        boolean passes = true;
        for (Map.Entry<String, Boolean> entry : permMap.entrySet()) {

            if (entry.getValue()) {

                if (!PermissionHandler.hasPermission(player, entry.getKey())) {

                    passes = false;
                    break;

                }

            } else {

                if (PermissionHandler.hasPermission(player, entry.getKey())) {

                    passes = false;
                    break;

                }

            }

        }

        return passes;

    }

}
