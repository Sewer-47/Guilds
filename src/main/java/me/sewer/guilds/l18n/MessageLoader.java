package me.sewer.guilds.l18n;

import me.sewer.guilds.GuildsPlugin;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

public class MessageLoader {

    private final GuildsPlugin plugin;

    public MessageLoader(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        File directory =  new File(plugin.getDataFolder(), "l18n");
        for (File file : directory.listFiles()) {
            this.plugin.getLogger().log(Level.INFO, "Loading language " + file.getName());
            this.load(file);
            this.plugin.getLogger().log(Level.INFO, "Language " + file.getName() + " was loaded succesful");
        }
    }

    private void load(File file) {
       String name = file.getName();
       String language = StringUtils.substring(name, 0 ,2);
       String country = StringUtils.substring(name, 3, 5);
       Locale locale = new Locale(language, country);
       MessageMap messageMap = new MessageMap(key -> "Missing " + key + " message.", locale);

       Properties properties = new Properties();
       try {
           InputStream inputStream = new FileInputStream(file);
           properties.load(inputStream);
           Collections.list(properties.keys()).forEach(key -> messageMap.put(key.toString(), properties.getProperty(key.toString())));
       } catch (IOException e) {
           this.plugin.getLogger().log(Level.SEVERE, "Cannot load language file " + name + " ", e);
       }
       this.plugin.getMessageManager().registerLocale(messageMap);
    }
}
