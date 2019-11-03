package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.elo.Elo;
import me.sewer.guilds.user.User;

public class ResetRankCommand extends Command {

    public static final String NAME = "resetrank";
    private final int defaultPoints;

    public ResetRankCommand(int defaultPoints) {
        super(NAME, "resetujRanking");
        this.defaultPoints = defaultPoints;
    }

    @Override
    public boolean onCommand(User user, String... args) {
        Elo elo = user.getElo();
        elo.setPoints(this.defaultPoints);
        user.sendMessage("eloReset");
        return true;
    }
}
