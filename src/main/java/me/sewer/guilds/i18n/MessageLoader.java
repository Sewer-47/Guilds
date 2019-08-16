package me.sewer.guilds.i18n;

import me.sewer.guilds.GuildsPlugin;
import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class MessageLoader {

    private final GuildsPlugin plugin;

    public MessageLoader(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        File directory =  new File(plugin.getDataFolder(), "i18n");
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
           properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
           Collections.list(properties.keys()).forEach(key -> messageMap.put(key.toString(), properties.getProperty(key.toString())));
       } catch (IOException e) {
           this.plugin.getLogger().log(Level.SEVERE, "Cannot load language file " + name + " ", e);
       }
       this.plugin.getMessageManager().registerLocale(messageMap);
    }

    public void unpack(File path) {
        File folder = new File(this.plugin.getDataFolder(), "i18n");
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            this.plugin.getLogger().log(Level.INFO, "Folder /Guilds/i18n/ was created!");
        }
        JarFile jarFile;
        try {
            jarFile = new JarFile(path);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error occurred while loading file caused by exception", e);
            return;
        }
        Enumeration enumEntries = jarFile.entries();
        while (enumEntries.hasMoreElements()) {
            JarEntry entry = (JarEntry) enumEntries.nextElement();
            File file = new File(this.plugin.getDataFolder() + File.separator + entry.getName());
            if (!Arrays.asList(folder.listFiles()).contains(file)) {
                if (!entry.isDirectory() && file.getName().endsWith(".properties")) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new BufferedInputStream(jarFile.getInputStream(entry));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte[] buffer = new byte[2048];
                        while (true) {
                            int nBytes = in.read(buffer);
                            if (nBytes <= 0) {
                                break;
                            }
                            out.write(buffer, 0, nBytes);
                        }
                        out.flush();
                    } catch (IOException e) {
                        this.plugin.getLogger().log(Level.SEVERE, "Error occurred while loading file" + file.getName() + "caused by exception", e);
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
