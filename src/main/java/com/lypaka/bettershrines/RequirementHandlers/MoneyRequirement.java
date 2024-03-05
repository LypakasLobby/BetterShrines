package com.lypaka.bettershrines.RequirementHandlers;

import com.lypaka.lypakautils.MiscHandlers.LogicalPixelmonMoneyHandler;
import net.minecraft.entity.player.ServerPlayerEntity;

public class MoneyRequirement {

    private final ServerPlayerEntity player;
    private final int amount;
    private final boolean chargesPlayer;

    public MoneyRequirement (ServerPlayerEntity player, int amount, boolean chargesPlayer) {

        this.player = player;
        this.amount = amount;
        this.chargesPlayer = chargesPlayer;

    }

    public boolean passes() {

        return LogicalPixelmonMoneyHandler.getBalance(this.player.getUniqueID()) >= this.amount;

    }

    public void payFeeIfNeeded() {

        if (this.chargesPlayer) {

            LogicalPixelmonMoneyHandler.remove(this.player.getUniqueID(), this.amount);

        }

    }

}
