package me.sewer.guilds.module;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public abstract class Module implements Listener {

    private String name;
    private boolean enabled;
    private GuildsPlugin plugin;

    public final void initialize(GuildsPlugin plugin) {
        this.plugin = plugin;

        ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
        if (info == null || info.name() == null) {
            throw new RuntimeException("Module must have have name");
        }
        this.name = info.name();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            throw new IllegalStateException(this.getName() + " already " + (enabled ? "enabled" : "disabled") + "!");
        }

        try {
            this.plugin.getLogger().log(Level.INFO, (enabled ? "Enabling" : "Disabling") + " module " + this.getName() + "...");
            if (enabled) {
                this.onEnable();
            } else {
                this.onDisable();
            }

            this.enabled = enabled;
        } catch (Throwable th) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not " + (enabled ? "enable" : "disable") + " " + this.getName() + ".", th);
        }
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public final String getName() {
        return this.name;
    }

    public GuildsPlugin getPlugin() {
        return plugin;
    }
}
