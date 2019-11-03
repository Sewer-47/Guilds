package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.impl.join.JoinRequest;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.Request;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class InviteCommand extends Command {

    public static final String NAME = "invite";
    private final UserManager userManager;
    private final MessageManager messageManager;
    private final GuildsPlugin plugin;

    public InviteCommand(GuildsPlugin plugin) {
        super(NAME, "dodaj", "zapros", "set");
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(User user, String[] args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        GuildMembers guildMembers = guild.getMembers();
        if (!guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.ADD)) {
            String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.ADD.getDisplayName());
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
            user.sendMessage("cantAddYourself");
            return true;
        }
        if (target.getGuild().equals(user.getGuild())) {
            user.sendMessage("playerIsInYourGuild");
            return true;
        }
        GuildRender render = guild.getRender();
        target.sendMessage("getRequest", render.getTag(), render.getName());
        user.sendMessage("requestSent", target.getUsername());
        Request request = new JoinRequest(target, guild, this.plugin);
        target.request(request); //set timeout
        return true;
    }
}
