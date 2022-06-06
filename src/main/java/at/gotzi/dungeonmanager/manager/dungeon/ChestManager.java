// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.dungeon;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonFileManager;
import at.gotzi.dungeonmanager.utils.Utils;
import com.magmaguy.elitemobs.items.customitems.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChestManager {

    private Main main;
    private DungeonFileManager dungeonFileManager;

    private List<ItemStack> chest = new ArrayList<>();

    public ChestManager(Main main) {
        this.main = main;
        this.dungeonFileManager = new DungeonFileManager(Main.getInstance());
    }

    public void setChests(String dungeon, Player player, World world) {
        YamlConfiguration dungeonConfig = dungeonFileManager.readDungeonFiles(dungeon);
        dungeonFileManager.getDungeonFile(dungeon);

        if(dungeonConfig.getConfigurationSection("chests") == null) return;
        for(String chest : dungeonConfig.getConfigurationSection("chests").getKeys(false)) {

            String loc = dungeonConfig.getString("chests." + chest + ".spawnLocation");
            int x = Integer.parseInt(loc.split(";")[0]);
            int y = Integer.parseInt(loc.split(";")[1]);
            int z = Integer.parseInt(loc.split(";")[2]);

            Block block = world.getBlockAt(x, y, z);
            block.setType(Material.CHEST);
            BlockFace blockFace = Utils.getBlockFace(Objects.requireNonNull(dungeonConfig.getString("chests." + chest + ".blockFace")));
            if(blockFace == null) {
                main.printErr("Invalid BlockFace type at Dungeon: " + dungeon + " Chest:" + chest);
                return;
            }
            Directional dir = (Directional) block.getBlockData();
            dir.setFacing(blockFace);
            block.setBlockData(dir);
            Chest cheststate = (Chest) block.getState();
            Inventory inventory = cheststate.getInventory();
            List<String> items = dungeonConfig.getStringList("chests." + chest + ".items");
            for (String item : items) {
                String itemFile = item.split(";")[0];
                int itemSlot = Integer.parseInt(item.split(";")[1]);
                int itemTier = Integer.parseInt(item.split(";")[2]);
                ItemStack itemStack = CustomItem.getCustomItem(itemFile).generateItemStack(itemTier, player);
                inventory.setItem(itemSlot, itemStack);
            }
        }
    }

    public void setChestDefault(Location location) {
        Block block = location.getBlock();
        location.getBlock().setType(Material.CHEST);
    }

}
