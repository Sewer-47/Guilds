package me.sewer.guilds.command.impl.create;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface ICreateOptions {

    Map<String, List<ItemStack>> requiredItems();

    int borderDistance();

    int spawnDistance();

    int guildDistance();

    List<String> allowedWorlds();

    boolean creatingEnabled();

    int tagMinLength();

    int tagMaxLength();

    int nameMinLength();

    int nameMaxLength();

    int regionSize();

    String guildSafeName();

    int guildSafeSize();

}
