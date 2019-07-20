package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class JoinCommand extends Command {

    public static final String NAME = "join";
    public static final String DESCRIPTION = "joing to guild";
    private final UserManager userManager;

    public JoinCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION, "dolacz");
        this.userManager = plugin.getUserManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (!user.acceptLastRequest()) {
                user.sendMessage("noRequests");
            }
        });
        return true;
    }
}
