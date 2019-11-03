package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class TopCommand extends Command {

    public static final String NAME = "top";
    private final MessageManager messageManager;
    private final GuildManager guildManager;
    private final UserManager userManager;

    public TopCommand(GuildsPlugin plugin) {
        this(plugin.getMessageManager(), plugin.getGuildManager(), plugin.getUserManager());
    }

    public TopCommand( MessageManager messageManager, GuildManager guildManager, UserManager userManager) {
        super(NAME, "topka");
        this.messageManager = messageManager;
        this.guildManager = guildManager;
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(User user, String... args) {

        return true;
    }
}