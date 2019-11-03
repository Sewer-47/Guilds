package me.sewer.guilds;

import java.io.File;

public class BaseFileManager {

	private GuildsPlugin plugin;
	private File directory;

	public BaseFileManager(GuildsPlugin plugin, File directory) {
	    this.plugin = plugin;
	    this.directory = directory;
	}

	public BaseFileManager(GuildsPlugin plugin, String directory) {
	    this(plugin, new File(plugin.getDataFolder(), directory));
	}

	public File getDirectory() {
	    return this.directory;
	}

}
