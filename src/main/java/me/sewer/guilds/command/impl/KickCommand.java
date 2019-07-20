package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.request.Request;
import me.sewer.guilds.request.guild.JoinRequest;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class KickCommand extends Command {

    public static final String NAME = "kick";
    public static final String DESCRIPTION = "kick from guild";
    private final UserManager userManager;

    public KickCommand(UserManager userManager) {
        super(NAME, DESCRIPTION, "wyrzuc");
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                if (user.getGuild().get().getMemebers().getOwner().equals(user)) {
                    if (args.length == 2) {
                        if (this.userManager.getUser(args[1]).isPresent()) {
                            User target = this.userManager.getUser(args[1]).get();
                            if (!target.equals(user)) {
                                Optional<Guild> guild = target.getGuild();
                                if (guild.isPresent() && guild.equals(user.getGuild())) {
                                    GuildRender render = guild.get().getRender();
                                    target.sendMessage("wasKicked", render.getTag(), render.getName());
                                    guild.get().getMemebers().getAll().forEach(member -> member.sendMessage("kickedPlayer", target.getName(), render.getTag(), render.getName(), user.getName()));
                                    guild.get().getMemebers().getAll().remove(target);
                                    target.setGuild(null);
                                } else {
                                    user.sendMessage("playerIsnInYourGuild");
                                }
                            } else {
                                user.sendMessage("cantKickYourself");
                            }
                        } else {
                            user.sendMessage("unkownPlayer");
                        }
                    } else {
                        user.sendMessage("correctUsage", "/guild kick <player>");
                    }
                } else {
                    user.sendMessage("mustBeOwner");
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
