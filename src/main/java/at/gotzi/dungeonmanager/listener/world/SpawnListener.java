package at.gotzi.dungeonmanager.listener.world;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnListener implements Listener {

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            ((LivingEntity)event.getEntity()).setAI(false);
        }
    }

    @EventHandler
    public void onSpawn2(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.PHANTOM) {
            event.setCancelled(true);
        }
    }
}
