package me.sewer.guilds.options;

import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateOptions implements ICreateOptions {

    private int borderDistance;
    private int spawnDistance;
    private int guildDistance;
    private List<String> allowedWorlds;
    private boolean creatingEnabled;
    private int regionSize;
    private int tagMinLength;
    private int tagMaxLength;
    private int nameMinLength;
    private int nameMaxLength;
    private String guildSafeName;
    private int guildSafeSize;
    private Multimap<String, ItemStack> requiredItems;

    public CreateOptions(Multimap<String, ItemStack> requiredItems) {
        this.requiredItems = requiredItems;
    }

    public void load(ConfigurationSection configuration) throws InvalidConfigurationException {
        ConfigurationSection render = configuration.getConfigurationSection("guildRender");

        if (!configuration.isInt("minDistanceFromBorder")) {
            throw new InvalidConfigurationException("border distance must be int");
        }

        if (!configuration.isInt("minDistanceToSpawn")) {
            throw new InvalidConfigurationException("spawn distance must be int");
        }

        if (!configuration.isInt("minDistanceBetweenGuilds")) {
            throw new InvalidConfigurationException("distance between guilds distance must be int");
        }

        if (!configuration.isInt("regionSize")) {
            throw new InvalidConfigurationException("region size must be int");
        }

        if (!render.isInt("tagMinLength") || !render.isInt("tagMaxLength")) {
            throw new InvalidConfigurationException("tag length must be int");
        }

        if (!render.isInt("nameMinLength") || !render.isInt("nameMaxLength")) {
            throw new InvalidConfigurationException("name length must be int");
        }

        if (!configuration.isBoolean("guildCreatingEnabled")) {
            throw new InvalidConfigurationException("guild creation must be boolean");
        }

        if (!configuration.isInt("guildSafeSize")) {
            throw new InvalidConfigurationException("guild safe size must be int");
        }

        int borderDistance = configuration.getInt("minDistanceFromBorder");
        int spawnDistance = configuration.getInt("minDistanceToSpawn");
        int guildDistance = configuration.getInt("minDistanceBetweenGuilds");
        int regionSize = configuration.getInt("regionSize");
        int tagMinLength = render.getInt("tagMinLength");
        int tagMaxLength = render.getInt("tagMaxLength");
        int nameMinLength = render.getInt("nameMinLength");
        int nameMaxLength = render.getInt("nameMaxLength");
        int guildSafeSize = configuration.getInt("guildSafeSize");

        if (borderDistance < 0) {
            throw new InvalidConfigurationException("border distance cannot be negative");
        }
        if (spawnDistance < 0) {
            throw new InvalidConfigurationException("spawn distance cannot be negative");
        }

        if (guildDistance < 0) {
            throw new InvalidConfigurationException("distance between guilds distance cannot be negative");
        }

        if (regionSize < 0) {
            throw new InvalidConfigurationException("region size cannot be negative");
        }

        if (tagMinLength < 0 || tagMaxLength < 0) {
            throw new InvalidConfigurationException("tag length cannot be negative");
        }

        if (nameMinLength < 0 || nameMaxLength < 0) {
            throw new InvalidConfigurationException("name length cannot be negative");
        }

        if (guildSafeSize < 0) {
            throw new InvalidConfigurationException("guild safe size cannot be negative");
        }

        if (guildSafeSize % 9 != 0) {
            throw new InvalidConfigurationException("guild safe size must be divisible by 9");
        }

        this.borderDistance = borderDistance;
        this.spawnDistance = spawnDistance;
        this.guildDistance = guildDistance;
        this.regionSize = regionSize;
        this.tagMinLength = tagMinLength;
        this.tagMaxLength = tagMaxLength;
        this.nameMinLength = nameMinLength;
        this.nameMaxLength = nameMaxLength;
        this.creatingEnabled = configuration.getBoolean("guildCreatingEnabled");
        this.guildSafeSize = guildSafeSize;
        this.guildSafeName = ChatColor.translateAlternateColorCodes('&', configuration.getString("guildSafeName"));
        this.allowedWorlds = configuration.getStringList("allowedWorlds");

    }


    @Override
    public Multimap<String, ItemStack> requiredItems() {
        return this.requiredItems;
    }

    @Override
    public int borderDistance() {
        return this.borderDistance;
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
