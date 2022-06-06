package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.BossManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonSetterTimer;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DeathListener implements Listener {

    public static HashMap<UUID, Location> players = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<Group> groups = PlayerData.getGroupRAM(player.getUniqueId());
        if (event.getEntity().getWorld().getName().contains(DungeonSetterTimer.getCurrentDungeon())) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            if (BossManager.hasBoss(player.getUniqueId()))
                BossManager.removeBoss(player.getUniqueId());
        }
        if (groups.contains(Group.TIME_MAGE) || player.hasPermission("groups.all")) {
            players.remove(player.getUniqueId());
            players.put(player.getUniqueId(), player.getLocation());
        }

        event.setDeathMessage(Messages.deathMsg.replace("%PLAYER%", player.getName()));
        player.teleport(Locations.lobbySpawn);
    }
}
