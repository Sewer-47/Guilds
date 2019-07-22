package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class KickCommand extends Command {

    public static final String NAME = "kick";
    public static final String DESCRIPTION = "kick from guild";
    private final UserManager userManager;
    private final MessageManager messageManager;

    public KickCommand(UserManager userManager, MessageManager messageManager) {
        super(NAME, DESCRIPTION, "wyrzuc");
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                GuildMembers guildMembers = user.getGuild().get().getMembers();
                if (guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.KICK)) {
                    if (args.length == 2) {
                        if (this.userManager.getUser(args[1]).isPresent()) {
                            User target = this.userManager.getUser(args[1]).get();
                            if (!target.equals(user)) {
                                Optional<Guild> guild = target.getGuild();
                                if (guild.isPresent() && guild.equals(user.getGuild())) {
                                    GuildRender render = guild.get().getRender();
                                    target.sendMessage("wasKicked", render.getTag(), render.getName());
                                    guild.get().getMembers().removePlayer(target.getUniqueId());
                                    target.setGuild(null);
                                    guild.get().getMembers().getAll().forEach(member -> {
                                        this.userManager.getUser(member).ifPresent(user1 -> user1.sendMessage("kickedPlayer", target.getName(), render.getTag(), render.getName(), user.getName()));
                                    });
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
                    String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.KICK.getDisplayName());
                    user.sendMessage("noPerm", perm);
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
