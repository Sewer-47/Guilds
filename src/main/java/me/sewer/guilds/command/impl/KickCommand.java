package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

import java.util.Optional;

public class KickCommand extends Command {

    public static final String NAME = "kick";
    private final UserManager userManager;
    private final MessageManager messageManager;

    public KickCommand(UserManager userManager, MessageManager messageManager) {
        super(NAME, "wyrzuc");
        this.userManager = userManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(User user, String[] args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        GuildMembers guildMembers = user.getGuild().get().getMembers();
        if (!guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.KICK)) {
            String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.KICK.getDisplayName());
            user.sendMessage("noPerm", perm);
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        if (!this.userManager.getUser(args[0]).isPresent()) {
            user.sendMessage("unkownPlayer");
            return true;
        }
        User target = this.userManager.getUser(args[0]).get();
        if (target.equals(user)) {
            user.sendMessage("cantKickYourself");
            return true;
        }
        Optional<Guild> guild = target.getGuild();
        if (!guild.isPresent() && !guild.equals(user.getGuild())) {
            user.sendMessage("playerIsnInYourGuild");
            return true;
        }
        GuildRender render = guild.get().getRender();
        target.sendMessage("wasKicked", render.getTag(), render.getName());
        guild.get().getMembers().removePlayer(target.getUniqueId());
        target.setGuild(null);
        guild.get().getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user1 -> user1.sendMessage("kickedPlayer", target.getUsername(), render.getTag(), render.getName(), user.getUsername()));
        });
        return true;
    }
}
