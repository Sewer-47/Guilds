package me.sewer.guilds.command.impl.items;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.user.User;
import me.sewer.guilds.window.Window;

public class ItemsCommand extends Command {

    public static final String NAME = "items";
    private final Window window;

    public ItemsCommand(Window window) {
        super(NAME, "itemy", "przedmioty");
        this.window = window;
    }

    @Override
    public boolean onCommand(User user, String... args) {
        this.window.open(user);
        return true;
    }
}
