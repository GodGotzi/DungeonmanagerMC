// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.dungeon;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DungeonFileManager {

    private Main main;

    public DungeonFileManager(Main main) {

    }

    public YamlConfiguration readDungeonFiles(String dungeon) {
        File dungeonFile = new File("plugins//DungeonManager//dungeons//" + dungeon + ".yml");
        if(!dungeonFile.exists()) {
            try {
                dungeonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dungeonFile);
        return config;
    }

    public File getDungeonFile(String dungeon) {
        File dungeonFile = new File("plugins//DungeonManager//dungeons//" + dungeon + ".yml");
        return dungeonFile;
    }

    public Set<String> getMobs(YamlConfiguration config) {
        ConfigurationSection mobs = config.getConfigurationSection("mobs");
        Set<String> s = new CopyOnWriteArraySet<>();
        if (mobs == null)
            return s;
        return mobs.getKeys(false);
    }

    public boolean mobsAvi(YamlConfiguration config) {
        ConfigurationSection configurationSection = config.getConfigurationSection("mobs");
        return configurationSection != null;
    }

    public boolean chestsAvi(YamlConfiguration config) {
        ConfigurationSection configurationSection = config.getConfigurationSection("chests");
        return configurationSection != null;
    }

    public Set<String> getChests(YamlConfiguration config) {
        ConfigurationSection mobs = config.getConfigurationSection("chests");
        Set<String> s = new CopyOnWriteArraySet<>();
        if (mobs == null)
            return s;
        return mobs.getKeys(false);
    }

    public Location getLocClassManager() {

        File file = new File("plugins//EliteMobs//npcs/" + Main.getInstance().getConfig().getString("classManagerNpc"));
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        String loc = yamlConfiguration.getString("spawnLocation");
        World world = Bukkit.getWorld(loc.split(",")[0]);
        double x = Double.parseDouble(loc.split(",")[1]);
        double y = Double.parseDouble(loc.split(",")[2]);
        double z = Double.parseDouble(loc.split(",")[3]);
        float yaw = Float.parseFloat(loc.split(",")[4]);
        float pitch = Float.parseFloat(loc.split(",")[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }
}