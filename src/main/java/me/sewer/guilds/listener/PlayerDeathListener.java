package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.elo.Elo;
import me.sewer.guilds.elo.EloAlgorithm;
import me.sewer.guilds.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final EloAlgorithm eloAlgorithm;
    private final UserManager userManager;

    public PlayerDeathListener(GuildsPlugin plugin) {
        this.eloAlgorithm = plugin.getEloAlgorithm();
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        this.userManager.getUser(event.getEntity()).ifPresent(victim -> {
            this.userManager.getUser(event.getEntity().getKiller()).ifPresent(killer -> {
                Elo victimElo = victim.getElo();
                Elo killerElo = killer.getElo();
                victim.getElo().setPoints(this.eloAlgorithm.calculate(victimElo, killerElo));
                killer.getElo().setPoints(this.eloAlgorithm.calculate(killerElo, victimElo));
            });
        });
    }
}
