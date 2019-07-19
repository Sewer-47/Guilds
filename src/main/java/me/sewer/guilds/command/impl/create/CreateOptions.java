package me.sewer.guilds.command.impl.create;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CreateOptions implements ICreateOptions {

    private final int spawnDistance;
    private final int guildDistance;
    private final List<String> allowedWorlds;
    private final boolean creatingEnabled;
    private final int tagMinLength;
    private final int tagMaxLength;
    private final int nameMinLength;
    private final int nameMaxLength;

    public CreateOptions(ConfigurationSection configuration) {
        ConfigurationSection render = configuration.getConfigurationSection("guildRender");
        this.spawnDistance = configuration.getInt("minDistanceToSpawn");
        this.guildDistance = configuration.getInt("minDistanceBetweenGuilds");
        this.allowedWorlds = configuration.getStringList("allowedWorlds");
        this.creatingEnabled = configuration.getBoolean("guildCreatingEnabled");
        this.tagMinLength = render.getInt("tagMinLength");
        this.tagMaxLength = render.getInt("tagMaxLength");
        this.nameMinLength = render.getInt("nameMinLength");
        this.nameMaxLength = render.getInt("nameMaxLength");
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
