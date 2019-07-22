package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.region.event.PlayerRegionEnterEvent;
import me.sewer.guilds.region.event.PlayerRegionQuitEvent;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GuildListeners implements Listener {

    private final UserManager userManager;
    private final boolean attack;

    public GuildListeners(GuildsPlugin plugin) {
        this(plugin.getUserManager(), plugin.getConfig().getBoolean("canAttackGuildMembers"));
    }

    public GuildListeners(UserManager userManager, boolean attack) {
        this.userManager = userManager;
        this.attack = attack;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        this.userManager.getUser(event.getDamager()).ifPresent(user -> {
            this.userManager.getUser(event.getEntity()).ifPresent(victim -> {
                user.getGuild().ifPresent(guild -> {
                    victim.getGuild().ifPresent(victimGuild -> {
                        if (guild.equals(victimGuild) && !attack) {
                            user.sendMessage("cannotAttack");
                            event.setCancelled(true);
                        }
                    });
                });
            });
        });
    }
}
