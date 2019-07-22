package me.sewer.guilds.command.impl.create;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CreateOptions implements ICreateOptions {

    private final int spawnDistance;
    private final int guildDistance;
    private final List<String> allowedWorlds;
    private final boolean creatingEnabled;
    private final int regionSize;
    private final int tagMinLength;
    private final int tagMaxLength;
    private final int nameMinLength;
    private final int nameMaxLength;
    private final String guildSafeName;
    private final int guildSafeSize;

    public CreateOptions(ConfigurationSection configuration) {
        ConfigurationSection render = configuration.getConfigurationSection("guildRender");
        this.spawnDistance = configuration.getInt("minDistanceToSpawn");
        this.guildDistance = configuration.getInt("minDistanceBetweenGuilds");
        this.allowedWorlds = configuration.getStringList("allowedWorlds");
        this.creatingEnabled = configuration.getBoolean("guildCreatingEnabled");
        this.regionSize = configuration.getInt("regionSize");
        this.tagMinLength = render.getInt("tagMinLength");
        this.tagMaxLength = render.getInt("tagMaxLength");
        this.nameMinLength = render.getInt("nameMinLength");
        this.nameMaxLength = render.getInt("nameMaxLength");
        this.guildSafeName = ChatColor.translateAlternateColorCodes('&', configuration.getString("guildSafeName"));
        this.guildSafeSize = configuration.getInt("guildSafeSize");
    }


    @Override
    public int spawnDistance() {
        return this.spawnDistance;
    }

    @Override
    public int guildDistance() {
        return this.guildDistance;
    }

    @Override
    public List<String> allowedWorlds() {
        return this.allowedWorlds;
    }

    @Override
    public boolean creatingEnabled() {
        return this.creatingEnabled;
    }

    @Override
    public int regionSize() {
        return this.regionSize;
    }

    @Override
    public String guildSafeName() {
        return this.guildSafeName;
    }

    @Override
    public int guildSafeSize() {
        return this.guildSafeSize;
    }

    @Override
    public int tagMinLength() {
        return this.tagMinLength;
    }

    @Override
    public int tagMaxLength() {
        return this.tagMaxLength;
    }

    @Override
    public int nameMinLength() {
        return this.nameMinLength;
    }

    @Override
    public int nameMaxLength() {
        return this.nameMaxLength;
    }
}
