package com.lypaka.bettershrines.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettershrines.API.ShrineActivateEvent;
import com.lypaka.bettershrines.RequirementHandlers.*;
import com.lypaka.bettershrines.ShrineRegistry.Shrine;
import com.lypaka.bettershrines.Utils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.lypaka.lypakautils.WorldStuff.WorldMap;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockInteractListener {

    @SubscribeEvent
    public void onBlockInteract (PlayerInteractEvent.RightClickBlock event) throws ObjectMappingException {

        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getHand() == Hand.OFF_HAND) return;

        int x = event.getPos().getX();
        int y = event.getPos().getY();
        int z = event.getPos().getZ();
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        String worldName = WorldMap.getWorldName(player);
        String location = worldName + "," + x + "," + y + "," + z;
        Shrine shrine = Shrine.getShrineFromLocation(location);

        if (shrine == null) return;
        if (shrine.getMaxActivationAmount() != 0) {

            String perm = shrine.getName().replace(".conf", "").toLowerCase();
            int amount = shrine.getMaxActivationAmount();
            if (PermissionHandler.hasPermission(player, "bs." + perm + "." + amount)) {

                player.sendMessage(FancyText.getFormattedText("&eYou've exceeded your maximum uses for this shrine!"), player.getUniqueID());
                return;

            }

        }
        Map<String, String> requirementsMap = shrine.getRequirementsMap();
        Map<String, Boolean> passMap = new HashMap<>();
        InventoryRequirement inventoryRequirement = null;
        PokemonRequirement pokemonRequirement = null;
        MoneyRequirement moneyRequirement = null;

        for (Map.Entry<String, String> requirement : requirementsMap.entrySet()) {

            if (requirement.getKey().equalsIgnoreCase("Dex-Percent")) {

                passMap.put("Dex-Percent", true);
                List<String> percents = shrine.getConfigManager().getConfigNode(0, "Requirements", "Dex-Percent").getList(TypeToken.of(String.class));
                for (String p : percents) {

                    boolean passes = DexPercentRequirement.passes(player, p);
                    if (!passes) {

                        passMap.put("Dex-Percent", false);
                        break;

                    }

                }

            } else if (requirement.getKey().equalsIgnoreCase("Inventory")) {

                Map<String, Map<String, String>> inventoryMap = shrine.getConfigManager().getConfigNode(0, "Requirements", "Inventory").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
                inventoryRequirement = new InventoryRequirement(player, inventoryMap);
                passMap.put("Inventory", inventoryRequirement.passes());

            } else if (requirement.getKey().equalsIgnoreCase("Moon-Phase")) {

                List<Integer> phases = shrine.getConfigManager().getConfigNode(0, "Requirements", "Moon-Phase").getList(TypeToken.of(Integer.class));
                passMap.put("Moon-Phase", MoonPhaseRequirement.pass(player, phases));

            } else if (requirement.getKey().equalsIgnoreCase("Permission")) {

                Map<String, Boolean> permMap = shrine.getConfigManager().getConfigNode(0, "Requirements", "Permission").getValue(new TypeToken<Map<String, Boolean>>() {});
                passMap.put("Permission", PermissionRequirement.passes(player, permMap));

            } else if (requirement.getKey().equalsIgnoreCase("Pokemon")) {

                Map<String, Map<String, String>> pokemonMap = shrine.getConfigManager().getConfigNode(0, "Requirements", "Pokemon").getValue(new TypeToken<Map<String, Map<String, String>>>() {});
                pokemonRequirement = new PokemonRequirement(player, pokemonMap);
                passMap.put("Pokemon", pokemonRequirement.passes());

            } else if (requirement.getKey().equalsIgnoreCase("Time")) {

                List<String> times = shrine.getConfigManager().getConfigNode(0, "Requirements", "Time").getList(TypeToken.of(String.class));
                passMap.put("Time", TimeRequirement.passes(player, times));

            } else if (requirement.getKey().equalsIgnoreCase("Weather")) {

                List<String> weathers = shrine.getConfigManager().getConfigNode(0, "Requirements", "Weather").getList(TypeToken.of(String.class));
                passMap.put("Weather", WeatherRequirement.passes(player, weathers));

            } else if (requirement.getKey().equalsIgnoreCase("Money")) {

                int amount = shrine.getConfigManager().getConfigNode(0, "Requirements", "Money", "Amount").getInt();
                boolean charges = false;
                if (!shrine.getConfigManager().getConfigNode(0, "Requirements", "Money", "Player-Pays").isVirtual()) {

                    charges = shrine.getConfigManager().getConfigNode(0, "Requirements", "Money", "Player-Pays").getBoolean();

                }
                moneyRequirement = new MoneyRequirement(player, amount, charges);
                passMap.put("Money", moneyRequirement.passes());

            }

        }

        boolean cancel = false;
        List<String> modulesFailed = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : passMap.entrySet()) {

            if (!entry.getValue()) {

                if (!cancel) {

                    cancel = true;

                }
                modulesFailed.add(entry.getKey());

            }

        }

        if (cancel) {

            event.setCanceled(true);
            String failed = String.join(", ", modulesFailed);
            player.sendMessage(FancyText.getFormattedText("&eYou've failed to meet the following requirements for this shrine: &c" + failed), player.getUniqueID());

        } else {

            PixelmonEntity pokemon = null;
            ArrayList<String> triggerCommands = new ArrayList<>(shrine.getTriggerExecution());
            if (shrine.getMode().equalsIgnoreCase("auto-start") || shrine.getMode().equalsIgnoreCase("lock")) {

                String cmd = null;
                for (String s : triggerCommands) {

                    if (s.contains("pokespawn") || s.contains("pokespawncoords")) {

                        cmd = s;
                        break;

                    }

                }
                if (cmd != null) {

                    String finalCmd = cmd;
                    triggerCommands.removeIf(c -> c.equalsIgnoreCase(finalCmd));
                    pokemon = Utils.buildPokemonFromCommand(cmd);
                    pokemon.setPositionAndUpdate(player.getPosX(), player.getPosY(), player.getPosZ());

                }

                PlayerPartyStorage storage = StorageProxy.getParty(player);
                if (storage.getFirstAblePokemon() == null) {

                    player.sendMessage(FancyText.getFormattedText("&eYou can't activate this shrine with a team of fainted Pokemon!"), player.getUniqueID());
                    return;

                }

            }
            ShrineActivateEvent shrineActivateEvent = new ShrineActivateEvent(player, shrine, pokemon, triggerCommands, inventoryRequirement, pokemonRequirement, moneyRequirement);
            MinecraftForge.EVENT_BUS.post(shrineActivateEvent);

        }

    }

}
