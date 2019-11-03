package me.sewer.guilds.command.impl.ally;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Relation;
import me.sewer.guilds.Request;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.user.UserManager;

public class AllyRequest implements Request {

    private final Guild owner;
    private final Guild guild;
    private final UserManager userManager;
    private final GuildsPlugin plugin;

    public AllyRequest(Guild owner, Guild guild, GuildsPlugin plugin) {
        this.owner = owner;
        this.guild = guild;
        this.userManager = plugin.getUserManager();
        this.plugin = plugin;
    }

    @Override
    public void deny() {
        GuildRender render = this.guild.getRender();
        this.owner.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user -> user.sendMessage("declinedAllyRequest", render.getTag(), render.getName()));
        });
    }

    @Override
    public void accept() {
        GuildRender render = this.guild.getRender();
        GuildRender ownerRender = this.guild.getRender();

        this.owner.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user -> user.sendMessage("acceptedAllyRequest", render.getTag(), render.getName()));
        });

        this.guild.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user -> user.sendMessage("nowAlly", ownerRender.getTag(), ownerRender.getName()));
        });
        this.owner.getRelations().set(this.guild.getUniqueId(), Relation.ALLY);
        this.guild.getRelations().set(this.owner.getUniqueId(), Relation.ALLY);
    }

    public Guild getGuild() {
        return this.guild;
    }
}
