package me.sewer.guilds.command;

import me.sewer.guilds.user.User;

import java.util.Arrays;
import java.util.List;

public class Command implements ICommand {

    private final String name;
    private final String description;
    private final String usage;
    private final List<String> aliases;

    public Command(String name, String... aliases) {
        this.name = name;
        this.description = name + "Description";
        this.usage = name + "Usage";
        this.aliases = Arrays.asList(aliases);
    }

    @Override
    public boolean onCommand(User user, String... args) {
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usage;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}