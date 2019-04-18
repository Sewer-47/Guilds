package me.sewer.guilds.command.commands;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.l18n.MessageManager;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class InfoCommand extends Command {

    public static final String NAME = "info";
    public static final String DESCRIPTION = "informacje o graczu";
    private final UserManager userManager;
    private final MessageManager messageManager;

    public InfoCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION);
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (Bukkit.getPlayer(args[1]) != null) {
            Player target = Bukkit.getPlayer(args[1]);
            this.userManager.getUser(player).ifPresent(user -> {
                this.userManager.getUser(target).ifPresent(targetUser -> {
                    Locale locale = user.getLocale();
                    String tag;
                    String name;
                    if (targetUser.getGuild().isPresent()) {
                        Guild guild = targetUser.getGuild().get();
                        GuildRender render = guild.getRender();
                        tag = this.messageManager.getMessage(locale, "guildTag", render.getTag());
                        name = this.messageManager.getMessage(locale, "guildName", render.getName());
                    } else {
                        String lack = this.messageManager.getMessage(locale, "lack");
                        tag = this.messageManager.getMessage(locale, "guildTag", lack);
                        name = this.messageManager.getMessage(locale, "guildName", lack);
                    }
                    user.sendMessage("playerInfo", target.getDisplayName(), targetUser.getPoints(), tag, name);
                });
            });
        }
        return true;
    }
}
