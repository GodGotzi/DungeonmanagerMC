package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.commands.AdminCommands.BuildCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.ChunkManager;
import at.gotzi.dungeonmanager.manager.player.InteractManager;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.manager.world.ResourceWorldsManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;

public class InteractListener implements Listener {

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Bed) {
                event.setCancelled(true);
            }
        }

        if (BuildCommand.hasBuildEnabled(player.getUniqueId())) return;

        InteractManager interactManager = new InteractManager(player.getUniqueId(), player.getLocation());
        if (!interactManager.isAllowedInteractBlock()) {
            event.setCancelled(true);
            player.sendMessage(Messages.restricted);
            return;
        }

        String worldName = player.getWorld().getName();
        if (worldName.contains("resource")) {
            if (worldName.contains("overworld")) {
                Location location = Locations.overworldSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.overWorldRange) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.inProtectionArea.replace("%RANGE%", String.valueOf((((int)location.distance(player.getLocation()) - ResourceWorldsManager.endRange)*-1))));
                }
            } else if (worldName.contains("nether")) {
                Location location = Locations.netherSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.netherRange) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.inProtectionArea.replace("%RANGE%", String.valueOf((((int)location.distance(player.getLocation()) - ResourceWorldsManager.endRange)*-1))));
                }
            } else if (worldName.contains("end")) {
                Location location = Locations.endSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.endRange) {
                    event.setCancelled(true);
                    player.sendMessage(Messages.inProtectionArea.replace("%RANGE%", String.valueOf((((int)location.distance(player.getLocation()) - ResourceWorldsManager.endRange)*-1))));
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null) {
                if (player.getWorld().getName().contains("openworld") || player.getWorld().getName().contains("resource")) {
                    if (ChunkManager.isBlocked(event.getItem().getType())) {
                        if (!ChunkManager.hasLeft(event.getItem().getType(), event.getClickedBlock().getChunk())) {
                            player.sendMessage(Messages.tooMuchItemsInChunk);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
