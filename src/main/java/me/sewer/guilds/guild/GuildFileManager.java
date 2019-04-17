package me.sewer.guilds.guild;

import me.sewer.guilds.BaseFileManager;
import me.sewer.guilds.GuildsPlugin;

import java.io.File;
import java.util.UUID;

public class GuildFileManager extends BaseFileManager {

    public GuildFileManager(GuildsPlugin plugin) {
        super(plugin, "guilds");
    }

    public File getGuildFile(Guild guild) {
        return this.getUserFile(guild.getUniqueId());
    }

    public File getUserFile(UUID uniqueId) {
        return new File(this.getDirectory(), uniqueId.toString() + ".yml");
    }
}