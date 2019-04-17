package me.sewer.guilds.guild;

public class GuildRender {

    private final String tag;
    private final String name;

    public GuildRender(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return this.tag;
    }

    public String getName() {
        return this.name;
    }

}