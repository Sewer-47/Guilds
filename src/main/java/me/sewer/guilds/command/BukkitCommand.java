package me.sewer.guilds.command;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class BukkitCommand implements CommandExecutor {

    private final Map<String, Command> commands = new HashMap<>();
    private final GuildsPlugin plugin;

    public BukkitCommand(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length >= 1) {
            Command command = this.commands.get(args[0]);
            if (command != null) {
                return command.onCommand(sender, args);
            } else {
                sender.sendMessage("Nie odnaleziono komendy");
                return true;
            }
        } else {
            for (Command command : commands.values()) {
                sender.sendMessage(command.getName() + " - " + command.getDescription());
            }
            return true;
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
