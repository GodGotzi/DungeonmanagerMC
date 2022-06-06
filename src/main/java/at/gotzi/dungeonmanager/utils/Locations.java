package at.gotzi.dungeonmanager.utils;

import at.gotzi.dungeonmanager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Locations {

    public static Location openWorldSpawn;
    public static Location lobbySpawn;
    public static Location overworldSpawn;
    public static Location netherSpawn;
    public static Location endSpawn;

    private final YamlConfiguration locationConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//locations.yml"));

    public Locations() {
        this.lobbySpawn();
        this.openWorldSpawn();
    }

    public void openWorldSpawn() {
        String loc = locationConfig.getString("openworldSpawn");
        World world = Bukkit.getWorld("openworld");
        double x = Double.parseDouble(loc.split(";")[0]);
        double y = Double.parseDouble(loc.split(";")[1]);
        double z = Double.parseDouble(loc.split(";")[2]);
        float yaw = Float.parseFloat(loc.split(";")[3]);
        float pitch = Float.parseFloat(loc.split(";")[4]);
        Location location = new Location(world, x, y, z, yaw, pitch);
        Locations.openWorldSpawn = location;
    }

    public void lobbySpawn() {
        String loc = locationConfig.getString("lobbySpawn");
        World world = Bukkit.getWorld("world");
        double x = Double.parseDouble(loc.split(";")[0]);
        double y = Double.parseDouble(loc.split(";")[1]);
        double z = Double.parseDouble(loc.split(";")[2]);
        float yaw = Float.parseFloat(loc.split(";")[3]);
        float pitch = Float.parseFloat(loc.split(";")[4]);
        Location location = new Location(world, x, y, z, yaw, pitch);
        Locations.lobbySpawn = location;
    }

}
