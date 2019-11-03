package me.sewer.guilds.region.tracker;

import me.sewer.guilds.region.Region;
import org.bukkit.entity.Player;

public interface RegionTrackerListener {

    void onEnter(Region region, Player player);

    void onQuit(Region region, Player player);
}
