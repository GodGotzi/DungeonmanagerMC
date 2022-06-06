// This class was created by Wireless


package at.gotzi.dungeonmanager.listener.world;

import at.gotzi.dungeonmanager.manager.world.DungeonWorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {

    @EventHandler(priority= EventPriority.HIGHEST)
    public void worldInit(WorldInitEvent event) {
        for (String dun : DungeonWorldManager.getWorldList()) {
            if (dun.contains(event.getWorld().getName())) {
                event.getWorld().setKeepSpawnInMemory(false);
            }
        }
    }
}