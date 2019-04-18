package me.sewer.guilds;

import me.sewer.guilds.command.BukkitCommand;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.commands.CreateCommand;
import me.sewer.guilds.guild.GuildFileManager;
import me.sewer.guilds.guild.GuildListeners;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.l18n.MessageListeners;
import me.sewer.guilds.l18n.MessageLoader;
import me.sewer.guilds.l18n.MessageManager;
import me.sewer.guilds.listener.AsyncPlayerChatListener;
import me.sewer.guilds.listener.GuildRegionListeners;
import me.sewer.guilds.listener.PlayerJoinListener;
import me.sewer.guilds.region.RegionListeners;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.region.RegionRegistry;
import me.sewer.guilds.region.RegionTask;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserFileManager;
import me.sewer.guilds.user.UserListeners;
import me.sewer.guilds.user.UserManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;

public class GuildsPlugin extends JavaPlugin {

    private UserManager userManager;
    private GuildManager guildManager;
    private RegionManager regionManager;
    private MessageManager messageManager;

    private BaseFileManager userFileManager;
    private BaseFileManager guildFileManager;


    @Override
    public void onEnable() {
        File folder = new File(this.getDataFolder(), "l18n");
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
            this.getLogger().log(Level.INFO, "Folder /Guilds/l18n/ was created!");
        }
        this.saveResource("l18n/en_US.properties", false);
        this.saveResource("l18n/pl_PL.properties", false);

        MessageLoader messageLoader = new MessageLoader(this);
        PluginManager pluginManager = this.getServer().getPluginManager();
        BukkitScheduler scheduler = this.getServer().getScheduler();

        this.saveDefaultConfig();

        this.userManager = new UserManager();
        this.guildManager = new GuildManager();
        this.regionManager = new RegionManager();

        String defaultLocale = this.getConfig().getString("defaultLang");
        String language = StringUtils.substring(defaultLocale, 0 ,2);
        String country = StringUtils.substring(defaultLocale, 3, 5);
        Locale locale = new Locale(language, country);
        this.messageManager = new MessageManager(locale);


        this.userFileManager = new UserFileManager(this);
        this.guildFileManager = new GuildFileManager(this);

        messageLoader.loadAll();

        for (World world : Bukkit.getWorlds()) {
            this.regionManager.addRegionRegistry(world.getUID(), new RegionRegistry(world.getUID()));
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player, this);
            this.userManager.registerUser(user);
        }

        scheduler.scheduleAsyncRepeatingTask(this, new RegionTask(this), 1L, 5L);

        for (Listener listener : new Listener[] {
                new UserListeners(this),
                new RegionListeners(this),
                new GuildListeners(this),
                new MessageListeners(this),

                new PlayerJoinListener(this),
                new AsyncPlayerChatListener(this),

                new GuildRegionListeners(this),

        }) {
            pluginManager.registerEvents(listener, this);
        }


        BukkitCommand command = new BukkitCommand(this);
        Command cmd = new CreateCommand(this);
        command.getCommands().put(cmd.getName(), cmd);
        this.getCommand("guild").setExecutor(command);
    }

    @Override
    public void onDisable() {


    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public GuildManager getGuildManager() {
        return this.guildManager;
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }

    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    public BaseFileManager getUserFileManager() {
        return this.userFileManager;
    }

    public BaseFileManager getGuildFileManager() {
        return this.guildFileManager;
    }
}
