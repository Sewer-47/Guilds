package me.sewer.guilds.command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Command unkown;
    private final Map<String, Command> byName = new HashMap<>();

    public CommandManager(Command unkown) {
        this.unkown = unkown;
    }

    public void registerCommand(Command command) {
        this.byName.put(command.getName(), command);
    }

    public void unregisterCommand(Command command) {
        this.byName.remove(command);
    }

    public Command getUnkown() {
        return this.unkown;
    }

    public Map<String, Command> getCommands() {
        return this.byName;
    }

    public Command get(String name) {
        for (Command command : byName.values()) {
            if (command.getName().equals(name) || command.getAliases().contains(name)) {
                return command;
            }
        }
        return this.unkown;
    }
}
