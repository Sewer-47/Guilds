package me.sewer.guilds.command.impl.join;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.user.User;

import java.util.Optional;

public class JoinCommand extends Command {

    public static final String NAME = "join";
    private final GuildManager guildManager;

    public JoinCommand(GuildsPlugin plugin) {
        super(NAME,  "dolacz");
        this.guildManager = plugin.getGuildManager();
    }

    @Override
    public boolean onCommand(User user, String[] args) {
        if (user.getLastRequest() instanceof JoinRequest) {
            if (!this.guildManager.getAll().contains(((JoinRequest) user.getLastRequest()).getGuild())) {
                user.setLastRequest(Optional.empty());
            }
        } else {
            user.setLastRequest(Optional.empty());
        }

        if (!user.acceptLastRequest()) {
            user.sendMessage("noRequests");
        }
        return true;
    }
}
