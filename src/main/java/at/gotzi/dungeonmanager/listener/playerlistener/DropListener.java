package at.gotzi.dungeonmanager.listener.playerlistener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(LoginListener.isTutorial(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
