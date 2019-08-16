package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Request;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.impl.ally.AllyRequest;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildRelations;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;

import java.util.Optional;

public class AcceptCommand extends Command {

    public static final String NAME = "accept";
    private final GuildManager guildManager;
    private final MessageManager messageManager;

    public AcceptCommand(GuildsPlugin plugin) {
        super(NAME, "akceptuj");
        this.guildManager = plugin.getGuildManager();
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();

        GuildMembers guildMembers = guild.getMembers();
        if (!guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.ACCEPT)) {
            String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.ACCEPT.getDisplayName());
            user.sendMessage("noPerm", perm);
            return true;
        }

        GuildRelations guildRelations = guild.getRelations();

        Request request = guildRelations.getLastRequest();
        if (request instanceof AllyRequest) {
            if (!this.guildManager.getAll().contains(((AllyRequest) request).getGuild())) {
                user.setLastRequest(Optional.empty());
            }
        } else {
            user.setLastRequest(Optional.empty());
        }

        if (!guildRelations.acceptLastRequest()) {
            user.sendMessage("noAllyRequests");
        }
        return true;
    }
}
