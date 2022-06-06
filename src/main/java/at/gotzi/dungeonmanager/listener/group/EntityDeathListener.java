package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.manager.file.PetManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if(PetManager.isPet(entity.getUniqueId())) {
            Player player = PetManager.getOwner(entity.getUniqueId());
            if(player == null) return;
            //player.sendMessage(Messages.);
            PetManager.removePet(player.getUniqueId());
        }
    }
}
