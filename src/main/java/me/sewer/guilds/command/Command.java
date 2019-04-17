package me.sewer.guilds.command;

import org.bukkit.command.CommandSender;

public class Command implements ICommand {

    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}