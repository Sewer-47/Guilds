package me.sewer.guilds.command.impl.create;

import me.sewer.guilds.GuildsPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CreateOptions implements ICreateOptions {

    private final int borderDistance;
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
    private final Map<String, List<ItemStack>> requiredItems;

    public CreateOptions(GuildsPlugin plugin, Map<String, List<ItemStack>> requiredItems) {
        ConfigurationSection configuration = plugin.getConfig();
        ConfigurationSection render = configuration.getConfigurationSection("guildRender");
        this.borderDistance = configuration.getInt("minDistanceFromBorder");
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
        this.requiredItems = requiredItems;
    }

    @Override
    public Map<String, List<ItemStack>> requiredItems() {
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
