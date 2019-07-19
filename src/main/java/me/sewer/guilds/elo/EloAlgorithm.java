package me.sewer.guilds.elo;

public class EloAlgorithm {

    private final int multiplier;

    public EloAlgorithm(int multiplier) {
        this.multiplier = multiplier;
    }

    public int calculate(Elo elo, Elo elo1) {
        return this.calculate(elo.getPoints(), elo1.getPoints());
    }

    public int calculate(int rankA, int rankB) {
        return (int) Math.ceil(rankA + multiplier * (1 - (1 / (1 + Math.pow(10, (rankB - rankA) / 400)))));
    }
}
