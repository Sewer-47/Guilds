package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;

public class InfoCommand extends Command {

    public static final String NAME = "info";
    private final UserManager userManager;
    private final MessageManager messageManager;

    public InfoCommand(GuildsPlugin plugin) {
        super(NAME);
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(User user, String... args) {
        Locale locale = user.getLocale();
        if (args.length != 1) {
            return false;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            user.sendMessage("unkownPlayer");
            return true;
        }
        this.userManager.getUser(target).ifPresent(targetUser -> {
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
            user.sendMessage("playerInfo", target.getDisplayName(), targetUser.getElo().getPoints(), tag, name);
        });
        return true;
    }
}
