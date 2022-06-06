package at.gotzi.dungeonmanager.manager.world;

import at.gotzi.dungeonmanager.manager.file.FileManager;
import at.gotzi.dungeonmanager.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ResourceWorldsManager extends FileManager {

    private static YamlConfiguration resourceConfig;

    public static int overWorldRange;
    public static int netherRange;
    public static int endRange;

    public ResourceWorldsManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//resourceWorlds.yml")));
        resourceConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//resourceWorlds.yml"));
    }

    @Override
    public void initialize() {
        String overworld = getStringNoColor("overworld.spawn");
        World world = Bukkit.getWorld(overworld.split(";")[0]);
        double x = Double.parseDouble(overworld.split(";")[1]);
        double y = Double.parseDouble(overworld.split(";")[2]);
        double z = Double.parseDouble(overworld.split(";")[3]);
        float yaw = Float.parseFloat(overworld.split(";")[4]);
        float pitch = Float.parseFloat(overworld.split(";")[5]);
        Locations.overworldSpawn = new Location(world, x, y, z, yaw, pitch);
        overWorldRange = getInt("overworld.protectionRange");

        String nether = getStringNoColor("nether.spawn");
        world = Bukkit.getWorld(nether.split(";")[0]);
        x = Double.parseDouble(nether.split(";")[1]);
        y = Double.parseDouble(nether.split(";")[2]);
        z = Double.parseDouble(nether.split(";")[3]);
        yaw = Float.parseFloat(nether.split(";")[4]);
        pitch = Float.parseFloat(nether.split(";")[5]);
        Locations.netherSpawn = new Location(world, x, y, z, yaw, pitch);
        netherRange = getInt("nether.protectionRange");

        String end = getStringNoColor("end.spawn");
        world = Bukkit.getWorld(end.split(";")[0]);
        x = Double.parseDouble(end.split(";")[1]);
        y = Double.parseDouble(end.split(";")[2]);
        z = Double.parseDouble(end.split(";")[3]);
        yaw = Float.parseFloat(end.split(";")[4]);
        pitch = Float.parseFloat(end.split(";")[5]);
        Locations.endSpawn = new Location(world, x, y, z, yaw, pitch);
        endRange = getInt("end.protectionRange");
    }
}
