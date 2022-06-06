// This class was created by Wireless


package at.gotzi.dungeonmanager;

import at.gotzi.dungeonmanager.commands.*;
import at.gotzi.dungeonmanager.commands.AdminCommands.BuildCommand;
import at.gotzi.dungeonmanager.commands.AdminCommands.SystemCommand;
import at.gotzi.dungeonmanager.commands.AdminCommands.SystemTabComplete;
import at.gotzi.dungeonmanager.commands.basic.SpawnCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.commands.group.commands.*;
import at.gotzi.dungeonmanager.commands.group.commands.home.HomeCommand;
import at.gotzi.dungeonmanager.commands.group.commands.home.RemoveHomeCommand;
import at.gotzi.dungeonmanager.commands.group.commands.home.SetHomeCommand;
import at.gotzi.dungeonmanager.commands.tabcompleter.*;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.listener.eventcaller.CallMove;
import at.gotzi.dungeonmanager.listener.group.*;
import at.gotzi.dungeonmanager.listener.playerlistener.*;
import at.gotzi.dungeonmanager.listener.world.*;
import at.gotzi.dungeonmanager.manager.EnchantmentManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonSetterTimer;
import at.gotzi.dungeonmanager.manager.file.*;
import at.gotzi.dungeonmanager.manager.groups.EffectManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.manager.world.DungeonWorldManager;
import at.gotzi.dungeonmanager.manager.world.ResourceWorldsManager;
import at.gotzi.dungeonmanager.manager.world.WorldManager;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.objects.enums.Error;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin {

    private static Main instance;
    private DungeonWorldManager dungeonWorldManager;
    private DungeonSetterTimer dungeonSetterTimer;

    public Economy eco;


    public static ItemStack fillUpItemStack;
    public static boolean fillup;
    public static String textureURl = null;
    public static boolean useCustomTextures = false;
    public static String PREFIX = "§3§l[§r§6DM§3§l] §9";
    public static boolean shutDown = false;

    private GroupManager groupManager;
    private SkillManager skillManager;
    private ShopManager shopManager;
    private AlchemistManager alchemistManager;
    private BasicConfigManager basicConfigManager;
    private AdvancementsManager advancementsManager;
    private JobManager jobManager;
    private ResourceWorldsManager resourceWorldsManager;
    private PetManager petManager;
    private OresManager oresManager;
    private ChunkManager chunkManager;
    private ScrapperManager scrapperManager;
    private EnchantmentManager enchantmentManager;
    private HunterFishManager hunterFishManager;


    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        try {

            List<String> customConfigs = new ArrayList<>();

            customConfigs.add("messages.yml");
            customConfigs.add("locations.yml");
            customConfigs.add("claims.yml");
            customConfigs.add("classes.yml");
            customConfigs.add("worlds.yml");
            customConfigs.add("basic.yml");
            customConfigs.add("skills.yml");
            customConfigs.add("shop.yml");
            customConfigs.add("alchemist.yml");
            customConfigs.add("jobs.yml");
            customConfigs.add("resourceWorlds.yml");
            customConfigs.add("advancements.yml");
            customConfigs.add("portals.yml");
            customConfigs.add("pets.yml");
            customConfigs.add("ores.yml");
            customConfigs.add("chunk.yml");
            customConfigs.add("scrapper.yml");
            customConfigs.add("hunterFish.yml");
            for (String file : customConfigs) {
                if (!new File("plugins//DungeonManager//" + file).exists()) {
                    saveResource(file, false);
                    Utils.info("Config " + file + " created!");
                }
                Utils.info("Config " + file + " loaded!");
            }

            this.groupManager = new GroupManager();
            this.skillManager = new SkillManager();
            this.shopManager = new ShopManager();
            this.alchemistManager = new AlchemistManager();
            this.basicConfigManager = new BasicConfigManager();
            this.advancementsManager = new AdvancementsManager();
            this.jobManager = new JobManager();
            this.resourceWorldsManager = new ResourceWorldsManager();
            this.oresManager = new OresManager();
            this.chunkManager = new ChunkManager();
            this.scrapperManager = new ScrapperManager();
            this.hunterFishManager = new HunterFishManager();

            if (!setupEconomy()) {
                print("You must have Vault installed!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            //fillup
            fillup = getConfig().getBoolean("fillInvs.bool");
            String itemName = getConfig().getString("fillInvs.itemName");
            int modelDataId = getConfig().getInt("fillInvs.modelDataId");
            Material material = Material.getMaterial(getConfig().getString("fillInvs.material"));
            if (material != null) {
                ItemStack itemStack = new ItemStack(material);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(itemName);
                itemMeta.setCustomModelData(modelDataId);
                itemStack.setItemMeta(itemMeta);
                fillUpItemStack = itemStack;
            } else {
                Utils.callError(getConfig().getName() + " " + "fillInvs", Error.FileSyntax);
            }

            saveDefaultConfig();

            useCustomTextures = getConfig().getBoolean("customTexturePack.useCustomTexturePack");
            if (useCustomTextures) {
                textureURl = getConfig().getString("customTexturePack.textureURL");
            }

            boolean dungeonsUse = getConfig().getBoolean("useMyDungeons");
            if (dungeonsUse) {
                if (!new File("plugins//DungeonManager//dungeons//hallosseum.yml").exists()) {
                    saveResource("hallosseum.yml", false);
                    saveResource("sewer_maze.yml", false);
                    new File("plugins//DungeonManager//dungeons").mkdirs();
                    File dungeon1 = new File("plugins//DungeonManager//hallosseum.yml");
                    File dungeon2 = new File("plugins//DungeonManager//sewer_maze.yml");
                    dungeon1.renameTo(new File("plugins//DungeonManager//dungeons//hallosseum.yml"));
                    dungeon2.renameTo(new File("plugins//DungeonManager//dungeons//sewer_maze.yml"));
                }
            }

            this.dungeonWorldManager = new DungeonWorldManager(this);
            this.dungeonSetterTimer = new DungeonSetterTimer(this, dungeonWorldManager);
            WorldManager worldManager = new WorldManager(this);

            dungeonSetterTimer.timerStart();
            worldManager.loadWorlds();

            new Messages();
            new Locations();
            this.registerCommands();
            this.registerEvents();
            this.registerEnchantments();

            ClaimManager.initialize();

            PlayerData.loadData();
            PlayerData.startPlayTime();
            PetManager.load();
            enchantmentManager = new EnchantmentManager();

            this.groupManager.initialize();
            this.skillManager.initialize();
            this.shopManager.initialize();
            this.alchemistManager.initialize();
            this.basicConfigManager.initialize();
            this.advancementsManager.initialize();
            this.jobManager.initialize();
            new EffectManager(this).runScheduler();
            this.resourceWorldsManager.initialize();
            this.oresManager.initialize();
            this.chunkManager.initialize();
            this.scrapperManager.initialize();
            this.hunterFishManager.initialize();
        }
        catch (Exception e) {
            Utils.DebugMessage("Error please conntact me!", 1);
            e.printStackTrace();
        }

        Utils.info("DungeonManager started successfully!");
    }

    public void newBasicConfig() {
        this.basicConfigManager.cancelTask();
        BasicConfigManager basicConfigManager = new BasicConfigManager();
        basicConfigManager.initialize();
        this.basicConfigManager = basicConfigManager;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economy = getServer().
                getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(economy != null)
            eco = economy.getProvider();
        return (eco != null);
    }

    public void registerCommands() {

        getCommand("skills").setExecutor(new SkillCommand());
        getCommand("dm").setExecutor(new MainCommand(this));
        getCommand("dm").setTabCompleter(new MainTabComplete());
        getCommand("world").setExecutor(new WorldCommand(this));
        getCommand("world").setTabCompleter(new WorldTabComplete());
        getCommand("portal").setExecutor(new PortalCommand(this));
        getCommand("portal").setTabCompleter(new PortalTabComplete());
        getCommand("party").setExecutor(new PartyCommand(this));
        getCommand("party").setTabCompleter(new PartyTabComplete());
        getCommand("c").setExecutor(new ClaimCommand(this));
        getCommand("c").setTabCompleter(new ClaimTabComplete());
        getCommand("dungeon").setTabCompleter(new DungeonCommand());
        getCommand("dungeon").setExecutor(new DungeonCommand());

        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpahereCommand(this));
        getCommand("tpa").setTabCompleter(new TpaCommand(this));
        getCommand("tpahere").setTabCompleter(new TpahereCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("near").setExecutor(new NearCommand(groupManager));
        getCommand("craft").setExecutor(new CraftCommand());
        getCommand("repairhand").setExecutor(new RepairCommand());
        getCommand("expwithdraw").setExecutor(new ExpWithDrawCommand());
        getCommand("stack").setExecutor(new StackCommand(this));
        getCommand("heal").setExecutor(new HealCommand(this));
        getCommand("back").setExecutor(new BackCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("nightvision").setExecutor(new NightVisionCommand());
        getCommand("ptime").setExecutor(new PTimeCommand());
        getCommand("pweather").setExecutor(new PWeatherCommand());
        getCommand("ptime").setTabCompleter(new PTimeCommand());
        getCommand("pweather").setTabCompleter(new PWeatherCommand());
        getCommand("telekinesis").setExecutor(new TeleCommand());
        getCommand("itemfilter").setExecutor(new ItemFilterCommand(this, groupManager));
        getCommand("itemfilter").setTabCompleter(new ItemFilterCommand(this, groupManager));
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("delhome").setExecutor(new RemoveHomeCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("home").setTabCompleter(new HomeCommand());
        getCommand("delhome").setTabCompleter(new RemoveHomeCommand());
        getCommand("pet").setExecutor(new PetCommand());
        getCommand("wild").setExecutor(new WildCommand());
        getCommand("hunt").setExecutor(new HunterFishCommand(this));
        getCommand("hunt").setTabCompleter(new HunterFishCommand(this));
        getCommand("system").setExecutor(new SystemCommand());
        getCommand("system").setTabCompleter(new SystemTabComplete());
        getCommand("huntonoff").setExecutor(new HuntOnOffCommand());

        getCommand("build").setExecutor(new BuildCommand());

        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("sitonplayer").setExecutor(new SitPlayerHead());
        getCommand("sitonplayer").setTabCompleter(new SitPlayerHead());
    }

    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new WorldChangeListener(this), this);
        pluginManager.registerEvents(new MoveListener(this, dungeonWorldManager, dungeonSetterTimer), this);
        pluginManager.registerEvents(new WorldListener(), this);
        pluginManager.registerEvents(new LoginListener(this), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new InteractListener(), this);
        pluginManager.registerEvents(new PlayerEntityListener(), this);
        pluginManager.registerEvents(new ExplosionListener(), this);
        pluginManager.registerEvents(new ExpThrowListener(), this);
        pluginManager.registerEvents(new BucketPlaceListener(), this);
        pluginManager.registerEvents(new BreakBlockListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new ItemListener(), this);
        pluginManager.registerEvents(new HitEntityEvent(), this);
        pluginManager.registerEvents(new PlaceBlockListener(), this);
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new InventoryInteractListener(this), this);
        pluginManager.registerEvents(new DropListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);

        pluginManager.registerEvents(new ExpListener(), this);
        pluginManager.registerEvents(new PaymentListener(), this);
        pluginManager.registerEvents(new PlayerHitListener(), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(), this);
        pluginManager.registerEvents(new EntityKillByPlayer(), this);
        pluginManager.registerEvents(new CommandListener(), this);
        pluginManager.registerEvents(new SpawnListener(), this);
        pluginManager.registerEvents(new FireballListener(), this);
        pluginManager.registerEvents(new ScrapperMenu.ScrapperMenuEvents(), this);

        pluginManager.registerEvents(new CallMove(), this);
    }

    private void registerEnchantments() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));
            Enchantment.registerEnchantment(glow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        shutDown = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String msg = Utils.color(getConfig().getString("kickMessageWhenReload"));
            PlayerData playerData = new PlayerData(player.getUniqueId());
            playerData.savePlayerDataStop(player.getWorld().getName());
            if (PetManager.hasPet(player))
                PetManager.removePet(player);
            ((CraftPlayer) player).kickPlayer(msg);
        }
        PetManager.killAllPets();

        this.unloadWorlds();
        if (HunterFishManager.getFish() != null) {
            Entity entity = Bukkit.getEntity(HunterFishManager.getFish());
            if (entity != null)
                entity.remove();
        }

        Bukkit.getScheduler().cancelTask(DungeonSetterTimer.getAsyncShed());
        DungeonSetterTimer.runAsyncTasks = false;
        if(DungeonWorldManager.getDungeonCount() >= getConfig().getInt("amountWorldFoldersCopied")) {
            dungeonWorldManager.deleteWorldFolders();
        }
    }



    public void unloadWorlds() {
        for (World world : Bukkit.getServer().getWorlds()) {
            Bukkit.unloadWorld(world, true);
        }
    }

    public String msgPrefix(String msg) {
        msg = PREFIX + msg;
        return msg;
    }

    public void printErr(String msg) {Bukkit.getLogger().warning(msg);}

    public void print(String msg) {
        Utils.info(msg);
    }

}