package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Relation;
import me.sewer.guilds.user.UserManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private final UserManager userManager;
    private final boolean guildAttack;
    private final boolean allyAttack;

    public EntityDamageByEntityListener(GuildsPlugin plugin) {
        this(plugin.getUserManager(), plugin.getConfig().getBoolean("canAttackGuildMembers"), plugin.getConfig().getBoolean("canAttackGuildFriends"));
    }

    public EntityDamageByEntityListener(UserManager userManager, boolean guildAttack, boolean allyAttack) {
        this.userManager = userManager;
        this.guildAttack = guildAttack;
        this.allyAttack = allyAttack;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        this.userManager.getUser(event.getDamager()).ifPresent(user -> {
            this.userManager.getUser(event.getEntity()).ifPresent(victim -> {
                user.getGuild().ifPresent(guild -> {
                    victim.getGuild().ifPresent(victimGuild -> {
                        if (guild.equals(victimGuild) && !guildAttack) {
                            user.sendMessage("cannotAttackMember");
                            event.setCancelled(true);
                            return;
                        }
                        Relation relation = guild.getRelations().relation(victimGuild.getUniqueId());
                        if (relation != null && relation.equals(Relation.ALLY) && !this.allyAttack) {
                            user.sendMessage("cannotAttackFriend");
                            event.setCancelled(true);
                            return;
                        }
                    });
                });
            });
        });
    }
}
