package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            player.sendMessage(ChatColor.RED + "Uzywasz pluginu Guilds " + GuildsPlugin.VERSION + ", ktory moze byc niestabilny, jesli nie uzywasz pluginu do testow, prosze uzyj wtyczki FunnyGuilds");
        }
    }
}
