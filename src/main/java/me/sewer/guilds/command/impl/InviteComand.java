package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.request.Request;
import me.sewer.guilds.request.guild.JoinRequest;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class InviteComand extends Command {

    public static final String NAME = "invite";
    public static final String DESCRIPTION = "sending join request";
    private final UserManager userManager;
    private final MessageManager messageManager;
    private final GuildsPlugin plugin;

    public InviteComand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION, "dodaj", "zapros", "add");
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                GuildMembers guildMembers = guild.getMembers();
                if (guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.ADD)) {
                    if (args.length == 2) {
                        if (this.userManager.getUser(args[1]).isPresent()) {
                            User target = this.userManager.getUser(args[1]).get();
                            if (!target.equals(user)) {
                                if (!target.getGuild().equals(user.getGuild())) {
                                    GuildRender render = guild.getRender();
                                    target.sendMessage("getRequest", render.getTag(), render.getName());
                                    user.sendMessage("requestSent", target.getName());
                                    Request request = new JoinRequest(target, guild, this.plugin);
                                    target.request(request); //add timeout
                                } else {
                                    user.sendMessage("playerIsInYourGuild");
                                }
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
                    String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.ADD.getDisplayName());
                    user.sendMessage("noPerm", perm);
                }
            } else {
               user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
