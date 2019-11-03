package me.sewer.guilds.user;

public class UserKDA {

    private int kills;
    private int deaths;
    private int assists;

    public UserKDA() {
        this.kills = 0;
        this.deaths = 0;
        this.assists = 0;
    }

    public double getKD() {
        return this.kills/this.assists;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }
}
