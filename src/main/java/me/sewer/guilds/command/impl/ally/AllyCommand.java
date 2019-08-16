package me.sewer.guilds.command.impl.ally;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Request;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildRelations;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class AllyCommand extends Command {

    public static final String NAME = "ally";
    private final GuildManager guildManager;
    private final UserManager userManager;
    private final MessageManager messageManager;
    private final GuildsPlugin plugin;

    public AllyCommand(GuildsPlugin plugin) {
        super(NAME,  "sojusz", "friend");
        this.guildManager = plugin.getGuildManager();
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
        GuildRelations guildRelations = guild.getRelations();
        if (!guildMembers.getMember(user).hasPermission(GuildPermission.ALLY)) {
            String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.ALLY.getDisplayName());
            user.sendMessage("noPerm", perm);
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        if (!this.guildManager.getGuild(args[0]).isPresent()) {
            user.sendMessage("unkownGuild");
            return true;
        }
        Guild target = this.guildManager.getGuild(args[0]).get();
        if (guildRelations.getFriends().contains(target)) {
            user.sendMessage("guildIsYourFriend");
            return true;
        }
        if (guild.equals(target)) {
            user.sendMessage("cantAddYourGuild");
            return true;
        }
        GuildRender render = guild.getRender();
        GuildRender targetRender = target.getRender();
        target.getMembers().getAll().forEach(guildMember -> {
            this.userManager.getUser(guildMember).ifPresent(guildUser -> guildUser.sendMessage("getAllyRequest", render.getTag(), render.getName()));
        });
        user.sendMessage("allyRequestSent", targetRender.getTag(), targetRender.getName());
        Request request = new AllyRequest(target, guild, this.plugin);
        target.getRelations().request(request); //add timeout
        return true;
    }
}
