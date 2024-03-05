package com.lypaka.bettershrines.ShrineRegistry;

import com.lypaka.bettershrines.BetterShrines;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;

import java.util.List;
import java.util.Map;

public class Shrine {

    private final String name;
    private final BasicConfigManager bcm;
    private final String mode;
    private final List<String> triggerExecution;
    private final List<String> triggerLocations;
    private final String triggerType;
    private ConfirmationMenu menu = null;
    private final int maxActivationAmount;
    private final Map<String, String> requirementsMap;

    public Shrine (String name, BasicConfigManager bcm, String mode, List<String> triggerExecution, List<String> triggerLocations, String triggerType, ConfirmationMenu menu, int maxActivationAmount, Map<String, String> requirementsMap) {

        this.name = name;
        this.bcm = bcm;
        this.mode = mode;
        this.triggerExecution = triggerExecution;
        this.triggerLocations = triggerLocations;
        this.triggerType = triggerType;
        this.menu = menu;
        this.maxActivationAmount = maxActivationAmount;
        this.requirementsMap = requirementsMap;

    }

    public void create() {

        BetterShrines.shrineMap.put(this.name, this);

    }

    public BasicConfigManager getConfigManager() {

        return this.bcm;

    }

    public String getName() {

        return this.name;

    }

    public String getMode() {

        return this.mode;

    }

    public List<String> getTriggerExecution() {

        return this.triggerExecution;

    }

    public List<String> getTriggerLocations() {

        return this.triggerLocations;

    }

    public String getTriggerType() {

        return this.triggerType;

    }

    public boolean hasConfirmationMenu() {

        return this.menu != null;

    }

    public ConfirmationMenu getMenu() {

        return this.menu;

    }

    public int getMaxActivationAmount() {

        return this.maxActivationAmount;

    }

    public Map<String, String> getRequirementsMap() {

        return this.requirementsMap;

    }

    public static Shrine getShrineFromLocation (String location) {

        Shrine shrine = null;
        for (Map.Entry<String, Shrine> entry : BetterShrines.shrineMap.entrySet()) {

            Shrine s = entry.getValue();
            if (s.getTriggerLocations().contains(location)) {

                shrine = s;
                break;

            }

        }

        return shrine;

    }

}
