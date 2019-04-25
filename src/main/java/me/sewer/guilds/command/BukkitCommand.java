package me.sewer.guilds.command;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.user.User;
import me.sewer.guilds.util.ChatUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class BukkitCommand implements CommandExecutor {

    private final Map<String, Command> commands = new HashMap<>();
    private final GuildsPlugin plugin;

    public BukkitCommand(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            User user = this.plugin.getUserManager().getUser(sender).get();
            if (args.length >= 1) {
                Command command = this.commands.get(args[0]);


                if (command != null) {
                    return command.onCommand(sender, args);
                } else {
                    user.sendMessage("noCommand");
                    return true;
                }
            } else {
                for (Command command : commands.values()) {
                    sender.sendMessage(command.getName() + " - " + command.getDescription());
                }
            }
        } else {
            ChatUtil.sendConsole(Level.INFO, "Only players can use this modules");
        }
        return true;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
