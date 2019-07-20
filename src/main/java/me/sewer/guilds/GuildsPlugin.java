package me.sewer.guilds;

import me.sewer.guilds.command.impl.*;
import me.sewer.guilds.elo.EloAlgorithm;
import me.sewer.guilds.guild.GuildRegionListeners;
import me.sewer.guilds.listener.*;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.command.BukkitCommand;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.impl.create.CreateCommand;
import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.GuildFileManager;
import me.sewer.guilds.guild.GuildListeners;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.i18n.MessageListeners;
import me.sewer.guilds.i18n.MessageLoader;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.module.impl.*;
import me.sewer.guilds.region.RegionListeners;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.region.RegionRegistry;
import me.sewer.guilds.region.RegionTask;
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

    private EloAlgorithm eloAlgorithm;


    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        MessageLoader messageLoader = new MessageLoader(this);
        PluginManager pluginManager = this.getServer().getPluginManager();
        BukkitScheduler scheduler = this.getServer().getScheduler();

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

        this.eloAlgorithm = new EloAlgorithm(this.getConfig().getInt("eloMultiplier"));

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

        scheduler.runTaskTimerAsynchronously(this, new RegionTask(this), 1L, 5L);
        scheduler.runTaskTimerAsynchronously(this, new GuildValidityTask(this), 1L, 20L * 6);

        for (Listener listener : new Listener[] {
                new UserListeners(this),
                new RegionListeners(this),
                new GuildListeners(this),
                new MessageListeners(this),

                new AsyncPlayerChatListener(this, messagesMap),
                new PlayerInteractEntityListener(this),

                new GuildRegionListeners(this),

                new EntityExplodeListener(this),

        }) {
            pluginManager.registerEvents(listener, this);
        }

        CreateOptions createOptions = new CreateOptions(this.getConfig());
        String prefix = this.getConfig().getString("privateChatPrefix");
        String format = this.getConfig().getString("privateChatFormat");

        this.modules = new HashSet<>();

        for (Module module : new Module[] {
                new CreationBanModule(createOptions),
                new TagLengthModule(createOptions),
                new NameLengthModule(createOptions),
                new WorldModule(createOptions),
                new SpawnModule(createOptions),
                new OtherGuildDistanceModule(createOptions),
                new PrivateChatModule(this.userManager, prefix, format),
        }) {
            module.initialize(this);
            module.setEnabled(true);
            this.modules.add(module);
        }

        BukkitCommand bukkitCommand = new BukkitCommand(this);
        for (Command command : new Command[] {
                new CreateCommand(this, createOptions),
                new InfoCommand(this),
                new AddComand(this.userManager, this.guildManager),
                new JoinCommand(this),
                new LeaveCommand(this.userManager),
                new KickCommand(this.userManager),
        }) {
            bukkitCommand.registerCommand(command);
        }
        this.getCommand("guild").setExecutor(bukkitCommand);
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

    public EloAlgorithm getEloAlgorithm() {
        return eloAlgorithm;
    }
}