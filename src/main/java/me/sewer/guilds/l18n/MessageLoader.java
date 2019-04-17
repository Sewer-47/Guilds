package me.sewer.guilds.l18n;

import me.sewer.guilds.GuildsPlugin;

import java.io.File;
import java.util.Locale;

public class MessageLoader {

    private final GuildsPlugin plugin;
    //https://4programmers.net/Java/Properties_-_pliki_tekstowe

    public MessageLoader(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll(File directory) {
        for (File file : directory.listFiles()) {
            this.load(file);
        }
    }

    public void load(File file) {
       
    }
}
