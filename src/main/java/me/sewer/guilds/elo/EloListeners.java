package me.sewer.guilds.elo;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.UserKDA;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Locale;

public class EloListeners implements Listener {

    private final EloAlgorithm eloAlgorithm;
    private final UserManager userManager;
    private final MessageManager messageManager;

    public EloListeners(GuildsPlugin plugin) {
        this.eloAlgorithm = plugin.getEloAlgorithm();
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity().getKiller() instanceof Player) {
            this.userManager.getUser(event.getEntity()).ifPresent(victim -> {
                this.userManager.getUser(event.getEntity().getKiller()).ifPresent(killer -> {
                    UserKDA killerKDA = killer.getKda();
                    UserKDA victimKDA = victim.getKda();
                    killerKDA.setKills(killerKDA.getKills() + 1);
                    victimKDA.setDeaths(victimKDA.getDeaths() + 1);
                    Elo victimElo = victim.getElo();
                    Elo killerElo = killer.getElo();
                    int newVictimElo = this.eloAlgorithm.calculateVictim(killerElo, victimElo);
                    int newKillerElo = this.eloAlgorithm.calculateKiller(killerElo, victimElo);
                    Player player = event.getEntity().getKiller();
                    Locale locale = killer.getLocale();
                    String killTitle = ChatColor.translateAlternateColorCodes('&', this.messageManager.getMessage(locale, "killTitle", newKillerElo - killerElo.getPoints() + ""));
                    String killSubTitle = ChatColor.translateAlternateColorCodes('&', this.messageManager.getMessage(locale, "killSubTitle", victim.getUsername()));
                    player.sendTitle(killTitle, killSubTitle, 5, 20, 5);
                    Bukkit.getOnlinePlayers().forEach(player1 -> {
                        this.userManager.getUser(player1).ifPresent(user -> {
                            String lack = this.messageManager.getMessage(user.getLocale(), "lack");
                            user.sendMessage("killMessage", killer.getUsername(), newKillerElo - killerElo.getPoints(), victim.getUsername(), victimElo.getPoints() - newVictimElo, lack);
                        });
                    });
                    killerElo.setPoints(newKillerElo);
                    victimElo.setPoints(newVictimElo);
                    event.setDeathMessage(null);
                });
            });
        }
    }
}
