package me.sewer.guilds;

import me.clip.placeholderapi.PlaceholderAPI;
import me.sewer.guilds.command.BukkitCommand;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.command.CommandManager;
import me.sewer.guilds.command.impl.*;
import me.sewer.guilds.command.impl.ally.AllyCommand;
import me.sewer.guilds.command.impl.create.CreateCommand;
import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.command.impl.items.ItemsCommand;
import me.sewer.guilds.command.impl.items.ItemsWindow;
import me.sewer.guilds.command.impl.join.JoinCommand;
import me.sewer.guilds.elo.EloAlgorithm;
import me.sewer.guilds.elo.EloListeners;
import me.sewer.guilds.guild.GuildFileManager;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildRegionListeners;
import me.sewer.guilds.hook.PlaceholderAPIHook;
import me.sewer.guilds.i18n.MessageListeners;
import me.sewer.guilds.i18n.MessageLoader;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.listener.AsyncPlayerChatListener;
import me.sewer.guilds.listener.EntityDamageByEntityListener;
import me.sewer.guilds.listener.EntityExplodeListener;
import me.sewer.guilds.listener.PlayerInteractEntityListener;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.impl.CreationBanModule;
import me.sewer.guilds.module.impl.PrivateChatModule;
import me.sewer.guilds.module.impl.RequiredItemsModule;
import me.sewer.guilds.module.impl.render.*;
import me.sewer.guilds.module.impl.world.BorderDistanceModule;
import me.sewer.guilds.module.impl.world.OtherGuildDistanceModule;
import me.sewer.guilds.module.impl.world.SpawnDistanceModule;
import me.sewer.guilds.module.impl.world.WorldTypeModule;
import me.sewer.guilds.region.RegionListeners;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.region.RegionRegistry;
import me.sewer.guilds.region.RegionTask;
import me.sewer.guilds.teleport.TeleportManager;
import me.sewer.guilds.teleport.TeleportTask;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserFileManager;
import me.sewer.guilds.user.UserListeners;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.validity.guild.GuildValidityTask;
import me.sewer.guilds.window.Window;
import me.sewer.guilds.window.WindowManager;
import me.sewer.guilds.window.listener.WindowBukkitListeners;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public final class GuildsPlugin extends JavaPlugin {

    private UserManager userManager;
    private GuildManager guildManager;
    private RegionManager regionManager;
    private MessageManager messageManager;
    private WindowManager windowManager;
    private CommandManager commandManager;
    private TeleportManager teleportManager;

    private BaseFileManager userFileManager;
    private BaseFileManager guildFileManager;

    private Set<Module> modules;

    private EloAlgorithm eloAlgorithm;

    private Items items;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        MessageLoader messageLoader = new MessageLoader(this);
        PluginManager pluginManager = this.getServer().getPluginManager();
        String defaultLocale = this.getConfig().getString("defaultLang");
        String language = StringUtils.substring(defaultLocale, 0, 2);
        String country = StringUtils.substring(defaultLocale, 3, 5);
        Locale locale = new Locale(language, country);

        this.userManager = new UserManager();
        this.guildManager = new GuildManager();
        this.regionManager = new RegionManager();
        this.windowManager = new WindowManager();
        this.commandManager = new CommandManager(new UnkownCommand());
        this.messageManager = new MessageManager(locale);
        this.teleportManager = new TeleportManager();
        this.userFileManager = new UserFileManager(this);
        this.guildFileManager = new GuildFileManager(this);
        this.eloAlgorithm = new EloAlgorithm(this.getConfig().getInt("eloMultiplier"));

        messageLoader.unpack(this.getFile().getAbsoluteFile());
        messageLoader.loadAll();

        Map<String, Material> itemsMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        File itemsFile = new File(this.getDataFolder(), "items.yml");
        this.saveResource("items.yml", false);
        FileConfiguration itemsConfiguration = YamlConfiguration.loadConfiguration(itemsFile);
        for (String key : itemsConfiguration.getKeys(false)) {
            Material material = Material.matchMaterial(itemsConfiguration.getString(key));
            if (material != null) {
                itemsMap.put(key, material);
            } else {
                this.getLogger().log(Level.SEVERE, "Cannot parse material " + key);
            }
        }
        this.items = new Items(itemsMap);

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

        Map<String, List<ItemStack>> requiredItems = new HashMap<>();
        ConfigurationSection creationItems = this.getConfig().getConfigurationSection("creationItems").getConfigurationSection("items");
        creationItems.getKeys(false).forEach(rank -> {
            if (!rank.equals("enabled")) {
                ConfigurationSection items = creationItems.getConfigurationSection(rank);
                List<ItemStack> itemsRank = new ArrayList<>();
                for (String key : items.getKeys(false)) {
                    Material material = this.items.getItem(key);
                    if (material != null) {
                        itemsRank.add(new ItemStack(material, items.getInt(key)));
                    } else {
                        this.getLogger().log(Level.SEVERE, "Cannot parse material " + key);
                    }
                }
                requiredItems.put("guilds.items." + rank, itemsRank);
            }
        });

        List<ItemStack> windowItems = new ArrayList<>();
        File windowsFile = new File(this.getDataFolder(), "windows.yml");
        this.saveResource("windows.yml", false);
        ConfigurationSection windowsConfiguration = YamlConfiguration.loadConfiguration(windowsFile).getConfigurationSection("items");
        String name = ChatColor.translateAlternateColorCodes('&', windowsConfiguration.getString("name"));
        int size = windowsConfiguration.getInt("size");
        for (String key : windowsConfiguration.getKeys(false)) {
            if (!key.equalsIgnoreCase("name") && !key.equalsIgnoreCase("size")) {
                Material material = this.items.getItem(windowsConfiguration.getConfigurationSection(key).getString("material"));
                int quantity = windowsConfiguration.getConfigurationSection(key).getInt("quantity");
                String itemName = windowsConfiguration.getConfigurationSection(key).getString("name");
                String lore = windowsConfiguration.getConfigurationSection(key).getString("lore");
                if (material != null) {
                    ItemStack itemStack = new ItemStack(material, quantity);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
                    itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore)));
                    itemStack.setItemMeta(itemMeta);
                    windowItems.add(itemStack);
                } else {
                    this.getLogger().log(Level.SEVERE, "Cannot parse material " + key);
                }
            }
        }

        ItemsWindow itemsWindow = new ItemsWindow(size, name, windowItems);

        CreateOptions createOptions = new CreateOptions(this, requiredItems);

        this.loadTask();
        this.loadListeners(pluginManager, messagesMap);
        this.loadModules(createOptions);
        this.loadCommands(createOptions, itemsWindow);

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            PlaceholderAPIHook placeHolderApiHook = new PlaceholderAPIHook(this);
            if (PlaceholderAPI.registerExpansion(placeHolderApiHook)) {
                this.getLogger().log(Level.INFO, "PlaceHolderAPI hook has been enabled succesful");
            }

        }
    }

    @Override
    public void onDisable() {

    }

    private void loadTask() {
        BukkitScheduler scheduler = this.getServer().getScheduler();
        scheduler.runTaskTimerAsynchronously(this, new RegionTask(this), 1L, 5L);
        scheduler.runTaskTimerAsynchronously(this, new GuildValidityTask(this), 1L, 20L * 6);
        scheduler.runTaskTimer(this, new TeleportTask(this.teleportManager.getTeleportList()), 1L, 20L);
    }

    private void loadModules(CreateOptions createOptions) {
        String prefix = this.getConfig().getString("privateChatPrefix");
        String format = this.getConfig().getString("privateChatFormat");

        this.modules = new HashSet<>();
        ConfigurationSection renderConfiguration = this.getConfig().getConfigurationSection("guildRender");
        for (Module module : new Module[]{
                new CreationBanModule(createOptions),
                new TagLengthModule(createOptions),
                new NameLengthModule(createOptions),
                new WorldTypeModule(createOptions),
                new SpawnDistanceModule(createOptions),
                new OtherGuildDistanceModule(createOptions),
                new PrivateChatModule(this.userManager, prefix, format),
                new BorderDistanceModule(createOptions),
                new TagBlackListModule(renderConfiguration.getStringList("tagBlackList")),
                new NameBlackListModule(renderConfiguration.getStringList("nameBlackList")),
                new DescriptionBlackListModule(renderConfiguration.getStringList("descriptionBlackList")),
                new RequiredItemsModule(createOptions),

        }) {
            module.initialize(this);
            module.setEnabled(true);
            this.modules.add(module);
        }
    }

    private void loadListeners(PluginManager pluginManager, Map<String, String> messagesMap) {
        Map<Material, Integer> materialIntegerMap = new HashMap<>();
        ConfigurationSection configurationSection = this.getConfig().getConfigurationSection("blocksCanBeDestroyedByTnt");
        for (String key : configurationSection.getKeys(false)) {
            Material material = this.items.getItem(key);
            if (material != null) {
                materialIntegerMap.put(material, configurationSection.getInt(key));
            } else {
                this.getLogger().log(Level.SEVERE, "Cannot parse material " + key);
            }
        }
        for (Listener listener : new Listener[]{
                new UserListeners(this),
                new RegionListeners(this),
                new EntityDamageByEntityListener(this),
                new MessageListeners(this),

                new AsyncPlayerChatListener(this, messagesMap),
                new PlayerInteractEntityListener(this),

                new GuildRegionListeners(this),

                new EntityExplodeListener(materialIntegerMap),

                new WindowBukkitListeners(this),

                new EloListeners(this),

        }) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private void loadCommands(CreateOptions createOptions, Window itemsWindow) {
        BukkitCommand bukkitCommand = new BukkitCommand(this);
        for (Command command : new Command[]{
                new CreateCommand(this, createOptions),
                new InfoCommand(this),
                new InviteCommand(this),
                new JoinCommand(this),
                new LeaveCommand(this.userManager),
                new KickCommand(this.userManager, this.getMessageManager()),
                new BaseCommand(this),
                new SafeCommand(),
                new SethomeCommand(this.messageManager),
                new DeleteCommand(this),
                new PermissionsCommand(),
                new AllyCommand(this),
                new AcceptCommand(this),
                new EnemyCommand(this),
                new BreakCommand(this),
                new ItemsCommand(itemsWindow),
        }) {
            this.commandManager.registerCommand(command);
        }

        this.getCommand("guild").setExecutor(bukkitCommand);
    }

    public TeleportManager getTeleportManager() {
        return this.teleportManager;
    }

    public Set<Module> getModules() {
        return this.modules;
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

    public WindowManager getWindowManager() {
        return this.windowManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public BaseFileManager getUserFileManager() {
        return this.userFileManager;
    }

    public BaseFileManager getGuildFileManager() {
        return this.guildFileManager;
    }

    public EloAlgorithm getEloAlgorithm() {
        return this.eloAlgorithm;
    }

    public Items getItems() {
        return this.items;
    }
}