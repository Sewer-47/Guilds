package me.sewer.guilds.request.guild;

import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.request.Request;
import me.sewer.guilds.user.User;

public class JoinRequest extends Request {

    private final Guild guild;

    public JoinRequest(User owner, Guild guild) {
        super(owner);
        this.guild = guild;
    }

    @Override
    public void deny() {
        this.guild.getMemebers().getAll().forEach(member -> {
            member.sendMessage("denitedJoinRequest", this.getOwner().getName());
        });
    }

    @Override
    public void accept() {
        this.getOwner().getBukkit().ifPresent(owner -> {
            if (!this.getOwner().getGuild().isPresent()) {
                this.getOwner().setGuild(this.guild);
                this.guild.getMemebers().getAll().add(this.getOwner());
                GuildRender render = this.guild.getRender();

                this.guild.getMemebers().getAll().forEach(member -> {
                    member.sendMessage("playerJoinedGuild", this.getOwner().getName());
                });

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
