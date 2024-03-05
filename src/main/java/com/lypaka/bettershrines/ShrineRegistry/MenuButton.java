package com.lypaka.bettershrines.ShrineRegistry;

import java.util.List;

public class MenuButton {

    private final String id;
    private final boolean cancels;
    private final List<String> commands;
    private final String displayText;
    private final int weight;

    public MenuButton (String id, boolean cancels, List<String> commands, String displayText, int weight) {

        this.id = id;
        this.cancels = cancels;
        this.commands = commands;
        this.displayText = displayText;
        this.weight = weight;

    }

    public String getID() {

        return this.id;

    }

    public boolean doesCancel() {

        return this.cancels;

    }

    public List<String> getCommands() {

        return this.commands;

    }

    public String getDisplayText() {

        return this.displayText;

    }

    public int getWeight() {

        return this.weight;

    }

}
