package me.sewer.guilds.module.impl.world;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ModuleInfo(name = "IntruderModule")
public class IntruderModule extends Module implements Runnable {

    private final GuildsPlugin plugin;
    private final UserManager userManager;
    private final Multimap<UUID, UUID> intruders = ArrayListMultimap.create();

    public IntruderModule(GuildsPlugin plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onRegionEnter(GuildRegionEnterEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        Guild guild = event.getGuild();
        this.userManager.getUser(playerId).ifPresent(user -> {
            user.getGuild().ifPresent(userGuild -> {
                if (!userGuild.equals(guild) && !userGuild.getRelations().isAlly(guild.getUniqueId())) {
                    this.intruders.put(guild.getUniqueId(), playerId);
                }
            });
        });
    }

    @EventHandler
    public void onRegionQuit(GuildRegionQuitEvent event) {
        Guild guild = event.getGuild();
        UUID playerId = event.getPlayer().getUniqueId();
        this.intruders.remove(guild, playerId);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this.plugin, this, 1L, 20 * 1L);
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, UUID> entry : this.intruders.entries()) {
            Optional<Guild> guild = this.plugin.getGuildManager().getGuild(entry.getKey());
            Optional<User> intruder = this.userManager.getUser(entry.getValue());
            if (!guild.isPresent()) {
                return;
            }
            for (UUID memberId : guild.get().getMembers().getAll()) {
                Optional<User> member = this.userManager.getUser(memberId);

                if (intruder.isPresent() && member.isPresent()) {
                    Optional<Player> player = intruder.get().getBukkit();
                    Optional<Player> memberPlayer = member.get().getBukkit();

                    if (player.isPresent() && memberPlayer.isPresent()) {
                        Location location = player.get().getLocation();
                        String messageFixed = ChatColor.translateAlternateColorCodes('&', this.plugin.getMessageManager().getMessage(member.get().getLocale(), "intruderAtGuildTerrain", player.get().getDisplayName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                        memberPlayer.get().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(messageFixed).create());
                    }
                }
            }
        }
    }
}


