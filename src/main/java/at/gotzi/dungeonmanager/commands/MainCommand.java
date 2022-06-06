// This class was created by Wireless


package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.dungeon.ChestManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonFileManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MainCommand implements CommandExecutor {

    private Main main;
    private static String id = "dungeonportalholo";
    private DungeonFileManager dungeonFileManager;
    private ChestManager chestManager;

    public MainCommand(Main main) {
        this.main = main;
        this.dungeonFileManager = new DungeonFileManager(main);
        this.chestManager = new ChestManager(main);
    }

    private File messagesFile = new File("plugins//DungeonManager//messages.yml");
    private YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("server.manage")) {
            sender.sendMessage(Messages.noPermission);
            return false;
        }
        if(sender instanceof Player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("lobbysetup")) {
                    Player player = (Player) sender;
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("sethologramspawn")) {
                            Location location = player.getLocation();
                            String loc = location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
                            main.getConfig().set("hologram.location", loc);
                            main.saveConfig();
                            NamedHologram hologram = null;
                            try {
                                hologram = CommandValidator.getNamedHologram(id);
                            } catch (CommandException e) {
                                e.printStackTrace();
                            }
                            hologram.teleport(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                            hologram.despawnEntities();
                            hologram.refreshAll();
                            HologramDatabase.saveHologram(hologram);
                            player.sendMessage(Messages.setCmd.replace("%SET%", "Hologram spawn"));
                        } else if (args[1].equalsIgnoreCase("setdungeonportalpos1")) {
                            Location location1 = player.getLocation();
                            Vector eyevec = player.getEyeLocation().getDirection();
                            RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location1, eyevec, 20);
                            if (rayTraceResult.getHitBlock() == null) {
                                player.sendMessage(Messages.noBlockTargeting);
                                return false;
                            }
                            Location location = rayTraceResult.getHitBlock().getLocation();
                            String loc = location.getX() + ";" + location.getY() + ";" + location.getZ();
                            main.getConfig().set("portal.pos1", loc);
                            main.saveConfig();
                            player.sendMessage(Messages.setCmd.replace("%SET%", "DungeonPortalPosition 1"));
                        } else if (args[1].equalsIgnoreCase("setdungeonportalpos2")) {
                            Location location1 = player.getLocation();
                            Vector eyevec = player.getEyeLocation().getDirection();
                            RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(location1, eyevec, 20);
                            if (rayTraceResult.getHitBlock() == null) {
                                player.sendMessage(Messages.noBlockTargeting);
                                return false;
                            }
                            Location location = rayTraceResult.getHitBlock().getLocation();
                            String loc = location.getX() + ";" + location.getY() + ";" + location.getZ();
                            main.getConfig().set("portal.pos1", loc);
                            main.saveConfig();
                            player.sendMessage(Messages.setCmd.replace("%SET%", "DungeonPortalPosition 1"));
                        }
                    } else
                        player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", args[0]));
                } else if (args[0].equalsIgnoreCase("dungeonsetup")) {
                    Player player = (Player) sender;
                    if (!player.hasPermission("dm.dungeonsetup")) {
                        player.sendMessage(Messages.noPermission);
                        return false;
                    }
                    if (args.length >= 3) {
                        if (args[2].equalsIgnoreCase("setMob")) {
                            if (args.length == 4) {
                                if (!Utils.getDungeonList().contains(args[1])) {
                                    sender.sendMessage("§4Invalid dungeon!");
                                    return false;
                                }
                                YamlConfiguration config = dungeonFileManager.readDungeonFiles(args[1]);
                                Location location = player.getLocation();
                                if (dungeonFileManager.mobsAvi(config)) {
                                    Set<String> mobs = dungeonFileManager.getMobs(config);
                                    String mobid = "mob" + String.valueOf(mobs.size() + 1);
                                    int x = (int) location.getX();
                                    int y = (int) location.getY();
                                    int z = (int) location.getZ();
                                    config.set("mobs." + mobid + ".spawnLocation", x + ";" + y + ";" + z + ";" + location.getYaw() + ";" + location.getPitch());
                                    boolean bool = false;
                                    if (args.length == 5) {
                                        if (args[3].contains("tier")) {
                                            bool = true;
                                        }
                                    } else if (!args[3].contains("tier")) {
                                        player.sendMessage(main.msgPrefix("You need to specify level!"));
                                        return false;
                                    }
                                    if (bool) {
                                        config.set("mobs." + mobid + ".type", args[3]);
                                    } else
                                        config.set("mobs." + mobid + ".type", args[3] + ";" + args[4]);
                                } else {
                                    String mobid = "mob" + String.valueOf(1);
                                    int x = (int) location.getX();
                                    int y = (int) location.getY();
                                    int z = (int) location.getZ();
                                    config.set("mobs." + mobid + ".spawnLocation", x + ";" + y + ";" + z + ";" + location.getYaw() + ";" + location.getPitch());
                                    boolean bool = false;
                                    if (args.length == 5) {
                                        if (args[3].contains("tier")) {
                                            bool = true;
                                        }
                                    } else if (!args[3].contains("tier")) {
                                        player.sendMessage(main.msgPrefix("You need to specify level!"));
                                        return false;
                                    }
                                    if (bool) {
                                        config.set("mobs." + mobid + ".type", args[3]);
                                    } else
                                        config.set("mobs." + mobid + ".type", args[3] + ";" + args[4]);
                                }
                                try {
                                    config.save(dungeonFileManager.getDungeonFile(args[1]));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(Messages.setCmd.replace("%SET%", "DungeonMob"));
                            }
                        } else if (args[2].equalsIgnoreCase("setChest")) {
                            if (!Utils.getDungeonList().contains(args[1])) {
                                sender.sendMessage("§4Invalid dungeon!");
                                return false;
                            }
                            if (args.length == 4) {
                                YamlConfiguration config = dungeonFileManager.readDungeonFiles(args[1]);
                                Location location = player.getLocation();
                                if (dungeonFileManager.chestsAvi(config)) {
                                    Set<String> chests = dungeonFileManager.getChests(config);
                                    String chestid = "chest" + String.valueOf(chests.size() + 1);
                                    int x = (int) location.getX();
                                    int y = (int) location.getY();
                                    int z = (int) location.getZ();
                                    config.set("chests." + chestid + ".spawnLocation", x + ";" + y + ";" + z);
                                } else {
                                    String chestid = "chests" + String.valueOf(1);
                                    int x = (int) location.getX();
                                    int y = (int) location.getY();
                                    int z = (int) location.getZ();
                                    config.set("chests." + chestid + ".spawnLocation", x + ";" + y + ";" + z);
                                }
                                try {
                                    config.save(dungeonFileManager.getDungeonFile(args[1]));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                chestManager.setChestDefault(location);
                                player.sendMessage(main.msgPrefix("You have set a dungeon chest, go into plugins/DungeonManager/dungeons" + args[1] + ".yml" + " to change the items in it!"));
                            } else
                                player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/dm dungeonsetup <dungeonname> setMob <type>"));
                        } else if (args[2].equalsIgnoreCase("import")) {
                            if (args.length == 4) {
                                if (!this.doesDungeonFileExists(args[1])) {
                                    sender.sendMessage("§cDungeon does not exist!");
                                    return false;
                                }
                                World.Environment environment = null;
                                if (args[3].equalsIgnoreCase("normal")) {
                                    environment = World.Environment.NORMAL;
                                } else if (args[3].equalsIgnoreCase("nether")) {
                                    environment = World.Environment.NETHER;
                                } else if (args[3].equalsIgnoreCase("end")) {
                                    environment = World.Environment.THE_END;
                                } else
                                    sender.sendMessage("§cInvalid Environment!");

                                File file = new File("plugins//DungeonManager//dungeons//" + args[1] + ".yml");
                                if (file.exists()) {
                                    sender.sendMessage("§cDungeon already exists!");
                                    return false;
                                } else {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                                main.getConfig().set("dungeons." + args[1] + ".environment", environment.toString());
                                main.saveConfig();
                                main.reloadConfig();

                                player.sendMessage("§1Dungeon has been imported!");
                            } else
                                sender.sendMessage(Messages.falseSyntaxCmd);
                        } else if (args[2].equalsIgnoreCase("setBoss")) {
                            if (!Utils.getDungeonList().contains(args[1])) {
                                sender.sendMessage("§4Invalid dungeon!");
                                return false;
                            }
                            YamlConfiguration config = dungeonFileManager.readDungeonFiles(args[1]);
                            Location location = player.getLocation();
                            int x = (int) location.getX();
                            int y = (int) location.getY();
                            int z = (int) location.getZ();
                            config.set("boss"+ ".spawnLocation", x + ";" + y + ";" + z + ";" + location.getYaw() + ";" + location.getPitch());
                            boolean bool = false;
                            if(args.length == 4) {
                                if(args[3].contains("tier")) {
                                    bool = true;
                                } else {
                                    sender.sendMessage(main.msgPrefix("You need to specify level!"));
                                    return false;
                                }
                            }

                            if(bool) {
                                config.set("boss" + ".type", args[3]);
                            } else {
                                config.set("boss" + ".type", args[3] + ";" + args[4]);
                            }

                            try {
                                config.save(dungeonFileManager.getDungeonFile(args[1]));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(Messages.setCmd.replace("%SET%", "DungeonBoss"));
                        } else if (args[2].equalsIgnoreCase("setspawn")) {
                            if (!Utils.getDungeonList().contains(args[1])) {
                                sender.sendMessage("§4Invalid dungeon!");
                                return false;
                            }
                            Location location = player.getLocation();
                            String loc = location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
                            main.getConfig().set("dungeons." + args[1] + ".spawn", loc);
                            main.saveConfig();
                            player.sendMessage(Messages.setCmd.replace("%SET%", "DungeonSpawn"));
                        } else if (args[2].equalsIgnoreCase("edit")) {
                            if (!Utils.getDungeonList().contains(args[1])) {
                                sender.sendMessage("§cDungeon does not exist");
                                return false;
                            }
                            if (!this.doesDungeonFileExists(args[1])) {
                                sender.sendMessage("§cDungeon does not exist!");
                                return false;
                            }

                            World.Environment environment = World.Environment.valueOf(main.getConfig().getString("dungeons." + args[1] + ".environment"));
                            if (environment == null) {
                                sender.sendMessage("§3Invalid environment look up to config.yml or ask me");
                                return false;
                            }
                            String loc = main.getConfig().getString("dungeons." + args[1] + ".spawn");
                            if (loc == null) {
                                sender.sendMessage("§cCouldn't find spawn location");
                                return false;
                            }

                            double x = Double.parseDouble(loc.split(";")[0]);
                            double y = Double.parseDouble(loc.split(";")[1]);
                            double z = Double.parseDouble(loc.split(";")[2]);
                            float yaw = Float.parseFloat(loc.split(";")[3]);
                            float pitch = Float.parseFloat(loc.split(";")[4]);
                            World world = new WorldCreator("dungeons//" + args[1]).environment(environment).createWorld();
                            player.teleport(new Location(world, x, y, z, yaw, pitch));
                            player.sendMessage("&1You got teleported!");

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (world.getPlayers().isEmpty()) {
                                        Bukkit.unloadWorld(world, true);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(main, 1000, 1000);
                        }
                    } else
                        sender.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/dm dungeonsetup <dungeonname> <command>"));
                }
            }
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    private boolean doesDungeonFileExists(String dungeon) {
        File dun = new File("dungeons/" + dungeon);
        return dun.exists();
    }

}
