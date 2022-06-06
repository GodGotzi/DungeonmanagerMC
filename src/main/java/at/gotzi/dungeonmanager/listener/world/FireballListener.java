package at.gotzi.dungeonmanager.listener.world;

import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FireballListener implements Listener {

    @EventHandler
    public void onFireball(EntityExplodeEvent event) {
        if (!event.getEntity().getWorld().getName().contains("resource"))
            if (event.getEntity() instanceof Fireball) event.setCancelled(true);
    }

}
