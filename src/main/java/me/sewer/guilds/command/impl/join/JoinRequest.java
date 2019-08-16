package me.sewer.guilds.command.impl.join;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Request;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.member.GuildMember;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class JoinRequest implements Request {

    private final User owner;
    private final Guild guild;
    private final UserManager userManager;

    public JoinRequest(User owner, Guild guild, GuildsPlugin plugin) {
        this.owner = owner;
        this.guild = guild;
        this.userManager = plugin.getUserManager();
    }

    @Override
    public void deny() {
        this.guild.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user -> user.sendMessage("denitedJoinRequest", this.owner.getUsername()));
        });
    }

    @Override
    public void accept() {
        this.owner.getBukkit().ifPresent(owner -> {
            if (!this.owner.getGuild().isPresent()) {
                GuildRender render = this.guild.getRender();

                this.guild.getMembers().getAll().forEach(member -> {
                    this.userManager.getUser(member).ifPresent(user -> user.sendMessage("playerJoinedGuild", this.owner.getUsername()));
                });

                this.owner.setGuild(this.guild);
                this.guild.getMembers().addPlayer(new GuildMember(this.owner));

                this.owner.sendMessage("joinedGuild", render.getTag(), render.getName());
            } else {
                this.owner.sendMessage("hasGuild");
            }
        });
    }

    public Guild getGuild() {
        return this.guild;
    }
}
