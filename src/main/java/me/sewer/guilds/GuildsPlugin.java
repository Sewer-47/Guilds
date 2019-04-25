package me.sewer.guilds;

import me.sewer.guilds.module.Module;
import me.sewer.guilds.command.BukkitCommand;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.commands.create.CreateCommand;
import me.sewer.guilds.command.commands.InfoCommand;
import me.sewer.guilds.command.commands.create.CreateOptions;
import me.sewer.guilds.guild.GuildFileManager;
import me.sewer.guilds.guild.GuildListeners;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.l18n.MessageListeners;
import me.sewer.guilds.l18n.MessageLoader;
import me.sewer.guilds.l18n.MessageManager;
import me.sewer.guilds.listener.AsyncPlayerChatListener;
import me.sewer.guilds.listener.PlayerInteractEntityListener;
import me.sewer.guilds.listener.GuildRegionListeners;
import me.sewer.guilds.listener.PlayerJoinListener;
import me.sewer.guilds.module.modules.*;
import me.sewer.guilds.region.RegionListeners;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.region.RegionRegistry;
import me.sewer.guilds.region.RegionTask;
import me.sewer.guilds.tablist.Tablist;
import me.sewer.guilds.tablist.TablistTask;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserFileManager;
import me.sewer.guilds.user.UserListeners;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.validity.guild.GuildValidityTask;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class GuildsPlugin extends JavaPlugin {

    private UserManager userManager;
    private GuildManager guildManager;
    private RegionManager regionManager;
    private MessageManager messageManager;

    private BaseFileManager userFileManager;
    private BaseFileManager guildFileManager;

    private Set<Module> modules;


    @Override
    public void onEnable() {
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

        messageLoader.unpack(this.getFile().getAbsoluteFile());
        messageLoader.loadAll();

        for (World world : Bukkit.getWorlds()) {
            this.regionManager.addRegionRegistry(world.getUID(), new RegionRegistry(world.getUID()));
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player, this);
            this.userManager.registerUser(user);
        }

        Map<String, String> messagesMap = new HashMap<>();
        ConfigurationSection configurationSection = this.getConfig().getConfigurationSection("chatVariables");
        for (String key : configurationSection.getKeys(false)) {
                messagesMap.put(key, configurationSection.getString(key));
        }

        Map<Integer, String> slots = new HashMap<>();
        slots.put(1, "hello");
        slots.put(2, "world");
        Tablist tablist = new Tablist(slots, "header", "footer");

        TablistTask tablistTask = new TablistTask(tablist, this.userManager);

        scheduler.runTaskTimerAsynchronously(this, tablistTask, 1L, 20L);
        scheduler.runTaskTimerAsynchronously(this, new RegionTask(this), 1L, 5L);
        scheduler.runTaskTimerAsynchronously(this, new GuildValidityTask(this), 1L, 20L * 6);

        for (Listener listener : new Listener[] {
                new UserListeners(this),
                new RegionListeners(this),
                new GuildListeners(this),
                new MessageListeners(this),

                new PlayerJoinListener(this),
                new AsyncPlayerChatListener(this, messagesMap),
                new PlayerInteractEntityListener(this),

                new GuildRegionListeners(this),

        }) {
            pluginManager.registerEvents(listener, this);
        }

        CreateOptions createOptions = new CreateOptions(this.getConfig());

        this.modules = new HashSet<>();

        for (Module module : new Module[] {
                new CreationBanModule(createOptions),
                new TagLengthModule(createOptions),
                new NameLengthModule(createOptions),
                new WorldModule(createOptions),
                new SpawnModule(createOptions),
                new OtherGuildDistanceModule(createOptions),
        }) {
            module.initialize(this);
            module.setEnabled(true);
            this.modules.add(module);
        }

        BukkitCommand command = new BukkitCommand(this);
        final Command createCommand = new CreateCommand(this, createOptions);
        command.getCommands().put(createCommand.getName(), createCommand);
        createCommand.getAliases().forEach(alias -> command.getCommands().put(alias, createCommand));

        final Command infoCommand = new InfoCommand(this);
        command.getCommands().put(infoCommand.getName(), infoCommand);
        infoCommand.getAliases().forEach(alias -> command.getCommands().put(alias, infoCommand));
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