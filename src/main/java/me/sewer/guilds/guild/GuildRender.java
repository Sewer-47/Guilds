package me.sewer.guilds.guild;

public class GuildRender {

    private final String tag;
    private final String name;
    private final String description;

    public GuildRender(String tag, String name, String description) {
        this.tag = tag;
        this.name = name;
        this.description = description;
    }

    public String getTag() {
        return this.tag;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}