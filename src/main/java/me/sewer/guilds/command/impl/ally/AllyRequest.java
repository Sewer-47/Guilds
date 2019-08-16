package me.sewer.guilds.command.impl.ally;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Request;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRelations;
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
        GuildRelations guildRelations = this.guild.getRelations();
        GuildRelations ownerRelations = this.owner.getRelations();
        this.owner.getRelations().getFriends().add(this.guild);
        this.guild.getRelations().getFriends().add(this.owner);
        if (guildRelations.getEnemies().contains(owner)) {
            guildRelations.getEnemies().remove(owner);
        }
        if (ownerRelations.getEnemies().contains(guild)) {
            ownerRelations.getEnemies().remove(guild);
        }
    }

    public Guild getGuild() {
        return this.guild;
    }
}
