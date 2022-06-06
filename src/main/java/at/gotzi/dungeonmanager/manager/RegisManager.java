// This class was created by Wireless


package at.gotzi.dungeonmanager.manager;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonFileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RegisManager {

    private Main main;
    private DungeonFileManager dungeonFileManager;
    private List<Player> unRegis = new ArrayList<>();
    
    public RegisManager(Main main) {
        this.main = main;
        this.dungeonFileManager = new DungeonFileManager(main);
    }

    public void sendRegisData(Player player) {
        Inventory inv = player.getInventory();

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setUnbreakable(true);
        compassMeta.setLodestone(dungeonFileManager.getLocClassManager());
        compassMeta.setLodestoneTracked(true);
        compass.setItemMeta(compassMeta);
        
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setUnbreakable(true);
        barrier.setItemMeta(barrierMeta);
        inv.setItem(4, compass);
        for (int i = 0; i < 34; i++) {
            inv.addItem(barrier);
        }
        unRegis.add(player);
    }

    public List<Player> getUnRegis() {
        return unRegis;
    }

    public void setUnRegis(List<Player> unRegis) {
        this.unRegis = unRegis;
    }

    public void addUnRegis(Player player) {
        unRegis.add(player);
    }

    public void removeUnRegis(Player player) {
        unRegis.remove(player);
    }
}
