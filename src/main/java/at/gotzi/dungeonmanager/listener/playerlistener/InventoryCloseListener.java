package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.manager.InventoryManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(InventoryManager.isRegistered(event.getInventory().hashCode())) {
            InventoryManager.unregisterInv(event.getInventory().hashCode());
        }
    }
}
