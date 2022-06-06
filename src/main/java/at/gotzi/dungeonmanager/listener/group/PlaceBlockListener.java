package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.manager.file.ChunkManager;
import at.gotzi.dungeonmanager.manager.player.InteractManager;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlaceBlockListener implements Listener {


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        InteractManager interactManager = new InteractManager(player.getUniqueId(), player.getLocation());
        if (!interactManager.isAllowedStructure()) {
            player.sendMessage(Messages.restricted);
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        if (ChunkManager.isBlocked(block.getType())) {
            if (!ChunkManager.hasLeft(block.getType(), block.getChunk())) {
                player.sendMessage(Messages.tooMuchItemsInChunk);
                event.setCancelled(true);
            }
        }



        if(block.getBlockData().getMaterial() == Material.SPAWNER) {
            ItemStack itemStack = event.getItemInHand();
            List<String> lore = itemStack.getItemMeta().getLore();
            if(lore == null) return;
            boolean bool = false;
            for (String l : lore) {
                if (l.contains("Entitytype: ")) {
                    bool = true;
                    break;
                }
            }
            if(bool) {
                String typeString = lore.get(0);
                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

                creatureSpawner.setSpawnedType(EntityType.valueOf(typeString.split(" ")[1]));
                creatureSpawner.update();
            }
        }
    }
}
