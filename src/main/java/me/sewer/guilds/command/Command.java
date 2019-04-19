package me.sewer.guilds.command;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class Command implements ICommand {

    private final String name;
    private final String description;
    private final List<String> aliases;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = Arrays.asList(aliases);
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

    public List<String> getAliases() {
        return aliases;
    }
}