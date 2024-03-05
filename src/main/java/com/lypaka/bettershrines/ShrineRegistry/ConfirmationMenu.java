package com.lypaka.bettershrines.ShrineRegistry;

import java.util.List;

public class ConfirmationMenu {

    private final MenuButton[] buttons;
    private final String title;
    private final List<String> text;

    public ConfirmationMenu (MenuButton[] buttons, String title, List<String> text) {

        this.buttons = buttons;
        this.title = title;
        this.text = text;

    }

    public MenuButton[] getButtons() {

        return this.buttons;

    }

    public String getTitle() {

        return this.title;

    }

    public List<String> getText() {

        return this.text;

    }

}
