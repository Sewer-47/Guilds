package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GuildRegionListeners implements Listener {

    private final GuildManager guildManager;
    private final UserManager userManager;
    private final List<Material> blockedMaterials;
    private final List<String> blockedCommands;

    public GuildRegionListeners(GuildsPlugin plugin) {
        this.guildManager = plugin.getGuildManager();
        this.userManager = plugin.getUserManager();
        this.blockedMaterials = new ArrayList<>();
        plugin.getConfig().getStringList("noForeignInteractions").forEach(string -> {
            Material material = Material.matchMaterial(string);
            if (material != null) {
                this.blockedMaterials.add(material);
            } else {
                plugin.getLogger().log(Level.SEVERE, "Cannot parse material " + string);
            }
        });
        this.blockedCommands = new ArrayList<>();
        plugin.getConfig().getStringList("blackListCommandsInGuildRegion").forEach(command -> {
            this.blockedCommands.add(command);
        });
    }

    @EventHandler
    public void onGuildRegionEnter(GuildRegionEnterEvent event) {
        Player player = event.getWho();
        this.userManager.getUser(player).ifPresent(user -> user.sendMessage("guildRegionEnter", event.getGuild().getRender().getTag()));
    }

    @EventHandler
    public void onGuildRegionQuit(GuildRegionQuitEvent event) {
        Player player = event.getWho();
        this.userManager.getUser(player).ifPresent(user -> user.sendMessage("guildRegionQuit", event.getGuild().getRender().getTag()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlock().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getAll().contains(user)) {
                    event.setCancelled(true);
                    user.sendMessage("cantBlockBreak");
                    return;
                }
            });
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlock().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getAll().contains(user)) {
                    event.setCancelled(true);
                    user.sendMessage("cantBlockPlace");
                    return;
                }
            });
        });
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer() != null) {
            this.userManager.getUser(player).ifPresent(user -> {
                this.guildManager.getGuild(event.getBlock().getLocation()).forEach(guild -> {
                    if (!guild.getMemebers().getAll().contains(user)) {
                        event.setCancelled(true);
                        user.sendMessage("cantBlockIgnite");
                        return;
                    }
                });
            });
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() != Material.AIR) {
            Player player = event.getPlayer();
            if (this.blockedMaterials.contains(block.getType())) {
                this.userManager.getUser(player).ifPresent(user -> {
                    this.guildManager.getGuild(block.getLocation()).forEach(guild -> {
                        if (!guild.getMemebers().getAll().contains(user)) {
                            user.sendMessage("cantBlockUse");
                            event.setCancelled(true);
                        }
                    });
                });
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlockClicked().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getAll().contains(user)) {
                    event.setCancelled(true);
                    user.sendMessage("cantUseBucket");
                    return;
                }
            });
        });
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlockClicked().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getAll().contains(user)) {
                    event.setCancelled(true);
                    user.sendMessage("cantUseBucket");
                    return;
                }
            });
        });
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] command = event.getMessage().substring(1).split(" ");
        if (this.blockedCommands.contains(command[0])) {
            Player player = event.getPlayer();
            this.userManager.getUser(player).ifPresent(user -> {
                this.guildManager.getGuild(player.getLocation()).forEach(guild -> {
                    if (!guild.getMemebers().getAll().contains(user)) {
                        event.setCancelled(true);
                        user.sendMessage("cantUseCommand");
                        return;
                    }
                });
            });
        }
    }
}
