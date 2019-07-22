package me.sewer.guilds.request.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMember;
import me.sewer.guilds.request.Request;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class JoinRequest extends Request {

    private final Guild guild;
    private final UserManager userManager;
    private final GuildsPlugin plugin;

    public JoinRequest(User owner, Guild guild, GuildsPlugin plugin) {
        super(owner);
        this.guild = guild;
        this.userManager = plugin.getUserManager();
        this.plugin = plugin;
    }

    @Override
    public void deny() {
        this.guild.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user -> user.sendMessage("denitedJoinRequest", this.getOwner().getName()));
        });
    }

    @Override
    public void accept() {
        this.getOwner().getBukkit().ifPresent(owner -> {
            if (!this.getOwner().getGuild().isPresent()) {
                GuildRender render = this.guild.getRender();

                this.guild.getMembers().getAll().forEach(member -> {
                    this.userManager.getUser(member).ifPresent(user -> user.sendMessage("playerJoinedGuild", this.getOwner().getName()));
                });

                this.getOwner().setGuild(this.guild);
                this.guild.getMembers().addPlayer(new GuildMember(this.getOwner(), this.plugin));

                this.getOwner().sendMessage("joinedGuild", render.getTag(), render.getName());
            } else {
                this.getOwner().sendMessage("hasGuild");
            }
        });
    }

    public Guild getGuild() {
        return guild;
    }
}
