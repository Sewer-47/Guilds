package me.sewer.guilds.command;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;

public class BukkitCommand implements CommandExecutor {

    private final GuildsPlugin plugin;
    private final CommandManager commandManager;
    private final me.sewer.guilds.command.CommandExecutor commandExecutor;
    private final MessageManager messageManager;

    public BukkitCommand(GuildsPlugin plugin) {
        this.plugin = plugin;
        this.commandManager = plugin.getCommandManager();
        this.messageManager = plugin.getMessageManager();
        this.commandExecutor = new me.sewer.guilds.command.CommandExecutor(this.commandManager, this.messageManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            User user = this.plugin.getUserManager().getUser(sender).get();
            Locale locale = user.getLocale();
            if (args.length >= 1) {
                this.commandExecutor.execute(args[0], user, Arrays.copyOfRange(args, 1, args.length));
            } else {
                for (Command command : this.commandManager.getCommands().values()) {
                    String message = this.messageManager.getMessage(locale, "format", this.messageManager.getMessage(locale, command.getName()), this.messageManager.getMessage(locale, command.getDescription()));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        } else {
            Bukkit.getLogger().log(Level.INFO, "Only players can use this command");
        }
        return true;
    }
}