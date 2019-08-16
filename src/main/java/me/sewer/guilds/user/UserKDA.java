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

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }
}
