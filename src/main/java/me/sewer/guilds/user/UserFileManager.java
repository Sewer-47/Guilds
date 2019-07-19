package me.sewer.guilds.user;

import java.io.File;
import java.util.UUID;

import me.sewer.guilds.BaseFileManager;
import me.sewer.guilds.GuildsPlugin;

public class UserFileManager extends BaseFileManager {

    public UserFileManager(GuildsPlugin plugin) {
        super(plugin, "users");
    }

    public File getUserFile(User user) {
        return this.getUserFile(user.getUniqueId());
    }

    public File getUserFile(UUID uniqueId) {
        return new File(this.getDirectory(), uniqueId.toString() + ".json");
    }   
}