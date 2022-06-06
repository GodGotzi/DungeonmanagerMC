// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.world;

import at.gotzi.dungeonmanager.commands.PortalCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class PortalManager {

    private YamlConfiguration pconfig = PortalCommand.getPconfig();

    public Location getPortalDest(String portal) {
        String loc = pconfig.getString("portals." + portal + ".destination");
        World world = Bukkit.getWorld(loc.split(";")[0]);
        double x = Double.parseDouble(loc.split(";")[1]);
        double y = Double.parseDouble(loc.split(";")[2]);
        double z = Double.parseDouble(loc.split(";")[3]);
        float yaw = Float.parseFloat(loc.split(";")[4]);
        float pitch = Float.parseFloat(loc.split(";")[5]);

        return new Location(world , x, y, z, yaw, pitch);
    }
}
