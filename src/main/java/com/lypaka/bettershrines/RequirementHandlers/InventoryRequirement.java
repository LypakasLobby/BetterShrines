package com.lypaka.bettershrines.RequirementHandlers;

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
            int metadata = 0;
            if (values.containsKey("Metadata")) {

                metadata = Integer.parseInt(values.get("Metadata"));

            }
            int slotIndex = 0;
            for (ItemStack slot : this.player.inventory.mainInventory) {

                String playerItemID = slot.getItem().getRegistryName().toString();
                int playerItemCount = slot.getCount();
                int playerItemMetadata = slot.getDamage();

                if (playerItemID.equalsIgnoreCase(id)) {

                    if (playerItemCount >= amount) {

                        if (playerItemMetadata == metadata) {

                            passMap.put(id, true);
                            if (values.containsKey("Sacrifice")) {

                                boolean sacrifice = Boolean.parseBoolean(values.get("Sacrifice"));
                                if (sacrifice) {

                                    this.sacrificeMap.put(slotIndex, amount);

                                }

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

}
