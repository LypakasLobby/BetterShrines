package com.lypaka.bettershrines.RequirementHandlers;

import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import com.pixelmonmod.pixelmon.api.storage.NbtKeys;
import com.pixelmonmod.pixelmon.items.PokeBallItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryRequirement {

    private final ServerPlayerEntity player;
    private final Map<String, Map<String, String>> itemMap;
    private final Map<Integer, Integer> sacrificeMap;

    public InventoryRequirement (ServerPlayerEntity player, Map<String, Map<String, String>> itemMap) {

        this.player = player;
        this.itemMap = itemMap;
        sacrificeMap = new HashMap<>();

    }

    public boolean passes() {

        Map<String, Boolean> passMap = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : itemMap.entrySet()) {

            String id = entry.getKey();
            passMap.put(id, false);
            Map<String, String> values = entry.getValue();
            int amount = 1;
            if (values.containsKey("Amount")) {

                amount = Integer.parseInt(values.get("Amount"));

            }
            int metadata = -1;
            if (values.containsKey("Metadata")) {

                metadata = Integer.parseInt(values.get("Metadata"));

            }
            String displayName = null;
            if (values.containsKey("Display-Name")) {

                displayName = values.get("Display-Name");

            }
            int slotIndex = 0;
            for (ItemStack slot : this.player.inventory.mainInventory) {

                String playerItemID = slot.getItem().getRegistryName().toString();
                if (slot.getItem() instanceof PokeBallItem) {

                    playerItemID = getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL(slot);

                }
                int playerItemCount = slot.getCount();
                int playerItemMetadata = slot.getDamage();

                if (playerItemID.equalsIgnoreCase(id)) {

                    if (playerItemCount >= amount) {

                        if (playerItemMetadata == metadata || metadata == -1) {

                            boolean namePasses = false;
                            if (displayName != null) {

                                if (slot.getDisplayName().getUnformattedComponentText().equalsIgnoreCase(FancyText.getFormattedText(displayName).getUnformattedComponentText())) {

                                    namePasses = true;

                                }

                            } else {

                                namePasses = true;

                            }
                            if (namePasses) {

                                passMap.put(id, true);
                                if (values.containsKey("Sacrifice")) {

                                    boolean sacrifice = Boolean.parseBoolean(values.get("Sacrifice"));
                                    if (sacrifice) {

                                        this.sacrificeMap.put(slotIndex, amount);

                                    }

                                }
                                break;

                            }

                        }

                    }

                }

                slotIndex++;

            }

        }

        boolean passes = true;
        for (Map.Entry<String, Boolean> entry : passMap.entrySet()) {

            if (!entry.getValue()) {

                passes = false;
                break;

            }

        }

        return passes;

    }

    public void removeIfNeeded() {

        for (Map.Entry<Integer, Integer> entry : this.sacrificeMap.entrySet()) {

            ItemStack item = this.player.inventory.mainInventory.get(entry.getKey());
            item.setCount(item.getCount() - entry.getValue());

        }

    }

    public static String getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL (ItemStack item) {

        String ball = "poke_ball";
        if (item.hasTag()) {

            ball = PokeBallRegistry.getPokeBall(item.getTag().getString(NbtKeys.POKE_BALL_ID)).getValue().get().getName();

        }

        return "pixelmon:" + ball.replace(" ", "_").toLowerCase();

    }

}
