package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.request.Request;
import me.sewer.guilds.request.guild.JoinRequest;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class AddComand extends Command {

    public static final String NAME = "add";
    public static final String DESCRIPTION = "sending join request";
    private final UserManager userManager;
    private final GuildManager guildManager;

    public AddComand(UserManager userManager, GuildManager guildManager) {
        super(NAME, DESCRIPTION, "dodaj", "zapros");
        this.userManager = userManager;
        this.guildManager = guildManager;
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
                                Guild guild = user.getGuild().get();
                                GuildRender render = guild.getRender();
                                target.sendMessage("getRequest", render.getTag(), render.getName());
                                user.sendMessage("requestSent", target.getName());
                                Request request = new JoinRequest(target, guild);
                                target.request(request); //add timeout
                            } else {
                                user.sendMessage("cantAddYourself");
                            }
                        } else {
                            user.sendMessage("unkownPlayer");
                        }
                    } else {
                        user.sendMessage("correctUsage", "/guild add <player>");
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
