package me.sewer.guilds.command.impl.create;

import java.util.List;

public interface ICreateOptions {

    int spawnDistance();

    int guildDistance();

    List<String> allowedWorlds();

    boolean creatingEnabled();

    int tagMinLength();

    int tagMaxLength();

    int nameMinLength();

    int nameMaxLength();

}
