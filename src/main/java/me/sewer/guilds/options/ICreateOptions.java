package me.sewer.guilds.options;

import com.google.common.collect.Multimap;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ICreateOptions {

    Multimap<String, ItemStack> requiredItems();

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
