package at.gotzi.dungeonmanager.listener.world;

import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.manager.world.ResourceWorldsManager;
import at.gotzi.dungeonmanager.utils.Locations;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHitListener implements Listener {

    public static List<UUID> lightningUUIDs = new ArrayList<>();

    @EventHandler
    public void onHit(EntityDamageEvent event) {
        if (PetManager.isPet(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }

        if (event.getEntity() instanceof Player player) {
            if (lightningUUIDs.contains(player.getUniqueId())) {
                if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                    event.setCancelled(true);
                }
            }

            String worldName = player.getWorld().getName();
            if (worldName.contains("resource")) {
                if (worldName.contains("overworld")) {
                    Location location = Locations.overworldSpawn;
                    location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                    if (location.distance(player.getLocation()) <= ResourceWorldsManager.overWorldRange) {
                        event.setCancelled(true);
                    }
                } else if (worldName.contains("nether")) {
                    Location location = Locations.netherSpawn;
                    location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                    if (location.distance(player.getLocation()) <= ResourceWorldsManager.netherRange) {
                        event.setCancelled(true);
                    }
                } else if (worldName.contains("end")) {
                    Location location = Locations.endSpawn;
                    location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                    if (location.distance(player.getLocation()) <= ResourceWorldsManager.endRange) {
                        event.setCancelled(true);
                    }
                }
            } else if (worldName.equals("world")) {
                event.setCancelled(true);
            }
        }
    }
}
