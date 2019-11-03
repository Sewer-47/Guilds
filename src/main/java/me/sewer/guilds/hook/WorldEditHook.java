package me.sewer.guilds.hook;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.vector.Vector3;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class WorldEditHook {

    private final GuildsPlugin plugin;

    public WorldEditHook(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    public void paste(Vector3 vector3, UUID worldId, File file, boolean air) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        World world = Bukkit.getWorld(worldId);

        if (world == null) {
            return;
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(vector3.getX(), vector3.getZ(), vector3.getY()))
                        .ignoreAirBlocks(air)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extract(File path) {
        File folder = this.plugin.getDataFolder();

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
                if (!entry.isDirectory() && file.getName().equalsIgnoreCase("build.schem")) {
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
