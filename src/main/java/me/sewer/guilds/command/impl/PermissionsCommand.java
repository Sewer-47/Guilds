package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class PermissionsCommand extends Command {

    private final UserManager userManager;

    public PermissionsCommand(UserManager userManager) {
        super("perm", "perms",  "uprawnienia", "perms");
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            Guild guild = user.getGuild().get();
            guild.getPermission().open(user);
        });
        return true;
    }
}
