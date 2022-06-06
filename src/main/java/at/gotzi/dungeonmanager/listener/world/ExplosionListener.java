package at.gotzi.dungeonmanager.listener.world;

import at.gotzi.dungeonmanager.utils.Locations;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class ExplosionListener implements Listener {


    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onExplosion(BlockExplodeEvent event) {
        if (event.getBlock().getWorld().getName().equals(Locations.openWorldSpawn.getWorld().getName()) || event.getBlock().getWorld().getName().equals(Locations.lobbySpawn.getWorld().getName())) {
            event.setCancelled(true);
        }
    }
}
