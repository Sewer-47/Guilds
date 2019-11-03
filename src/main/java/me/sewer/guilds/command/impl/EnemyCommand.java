package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Relation;
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
import org.bukkit.Bukkit;

import java.util.UUID;

public class EnemyCommand extends Command {

    public static final String NAME = "enemy";
    private final GuildManager guildManager;
    private final UserManager userManager;
    private final MessageManager messageManager;

    public EnemyCommand(GuildsPlugin plugin) {
        super(NAME, "wrog");
        this.guildManager = plugin.getGuildManager();
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        if (args.length != 1) {
            return false;
        }
        this.guildManager.getGuild(args[0]).ifPresentOrElse(target -> {

            Guild guild = user.getGuild().get();

            UUID targetId = target.getUniqueId();
            UUID guildId = guild.getUniqueId();

            GuildMembers guildMembers = guild.getMembers();
            if (!guildMembers.getMember(user.getUniqueId()).hasPermission(GuildPermission.ENEMY)) {
                String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.ENEMY.getDisplayName());
                user.sendMessage("noPerm", perm);
                return;
            }

            if (target.equals(guild)) {
                user.sendMessage("cantSetEnemyYourGuild");
                return;
            }
            GuildRelations guildRelations = guild.getRelations();
            Relation relation = guild.getRelations().relation(targetId);
            if (!guildRelations.isAlly(targetId)) {
                user.sendMessage("StillEnemies");
                return;
            }

            GuildRender guildRender = guild.getRender();


            GuildRender targetRender = target.getRender();
            guildRelations.set(targetId, Relation.ENEMY);
            target.getRelations().set(guildId, Relation.ENEMY);
            Bukkit.getOnlinePlayers().forEach(player -> {
                this.userManager.getUser(player).ifPresent(user1 -> user1.sendMessage("guildsIsEnemies",
                        guildRender.getTag(),
                        guildRender.getName(),
                        targetRender.getTag(),
                        targetRender.getName()
                ));
            });
        }, () -> user.sendMessage("unkownGuild"));
        return true;
    }
}
