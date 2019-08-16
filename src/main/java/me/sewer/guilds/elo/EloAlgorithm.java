package me.sewer.guilds.elo;

public class EloAlgorithm {

    private final int multiplier;

    public EloAlgorithm(int multiplier) {
        this.multiplier = multiplier;
    }

    public int calculateKiller(Elo elo0, Elo elo1) {
        return this.calculateKiller(elo0.getPoints(), elo1.getPoints());
    }

    public int calculateVictim(Elo elo0, Elo elo1) {
        return this.calculateVictim(elo0.getPoints(), elo1.getPoints());
    }

    public int calculateKiller(int rank0, int rank1) {
        return rank0 + this.multiplier * (1 - (1 / (1 + 10^((rank1 -rank0) / 400))));
    }

    public int calculateVictim(int rank0, int rank1) {
        return rank1 + this.multiplier * (0 - (1 / (1 + 10^((rank0 -rank1) / 400))));
    }
}
