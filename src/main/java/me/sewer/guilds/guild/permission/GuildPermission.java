package me.sewer.guilds.guild.permission;

public class GuildPermission {

    private final String name;
    private final String displayName;

    public GuildPermission(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public static final GuildPermission KICK = new GuildPermission("kick", "kickingPlayers");

    public static final GuildPermission ADD = new GuildPermission("add",  "addingPlayers");

    public static final GuildPermission OPEN_SAFE = new GuildPermission("openSafe",  "openingSafe");

    public static final GuildPermission MANAGE_SAFE = new GuildPermission("manageSafe",  "managingSafe");

    public static final GuildPermission SETHOME = new GuildPermission("sethome",  "settingHome");

    public static final GuildPermission ALLY = new GuildPermission("ally",  "addingAlliance");

    public static final GuildPermission ACCEPT = new GuildPermission("accept",  "acceptAlliance");

    public static final GuildPermission BREAK = new GuildPermission("break",  "breakAlliance");

    public static final GuildPermission ENEMY = new GuildPermission("enemy",  "addingEnemy");

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
