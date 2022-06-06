package at.gotzi.dungeonmanager.listener.world;

import at.gotzi.dungeonmanager.manager.BossManager;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillByPlayer implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Entity dead = e.getEntity();
        if (BossManager.isDungeonBoss(dead.getUniqueId())) {
            Player player = BossManager.getPlayer(dead.getUniqueId());
            if (BossManager.hasBoss(player.getUniqueId())) {
                if (BossManager.isBoss(player.getUniqueId(), dead.getUniqueId())) {
                    BossManager.playTitle(player.getWorld().getName(), player.getName());
                }
            }
        }
    }
}
