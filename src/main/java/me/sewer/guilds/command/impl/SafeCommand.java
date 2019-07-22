package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class SafeCommand extends Command {

    public static final String NAME = "safe";
    public static final String DESCRIPTION = "open guild safe";
    private final UserManager userManager;

    public SafeCommand(UserManager userManager) {
        super(NAME, DESCRIPTION, "schowek", "sejf");
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                guild.getSafe().open(user);
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
