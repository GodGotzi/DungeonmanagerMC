// This class was created by Wireless


package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.listener.group.FoodLevelChangeListener;
import at.gotzi.dungeonmanager.manager.BossManager;
import at.gotzi.dungeonmanager.manager.file.AdvancementsManager;
import at.gotzi.dungeonmanager.manager.file.BasicConfigManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData.setWorld(player.getUniqueId(), event.getPlayer().getWorld().getName());
        PlayerData playerData = new PlayerData(player.getUniqueId());
        if (Main.shutDown)
            playerData.savePlayerDataStop(event.getPlayer().getWorld().getName());
        else
         playerData.savePlayerData(event.getPlayer().getWorld().getName());
        AdvancementsManager.saveProgress(player);
        BasicConfigManager.unloadNews(player);
        PetManager.removePet(player);
        FoodLevelChangeListener.removeFoodLevel(player);

        String worldName = event.getPlayer().getWorld().getName();
        boolean bool = false;
        for (String dun : Utils.getDungeonList()) {
            if (worldName.contains(dun)) {
                bool = true;
                break;
            }
        }

        if(bool) {
            if (BossManager.hasBoss(player.getUniqueId()))
                BossManager.removeBoss(player.getUniqueId());
            if (event.getPlayer().getWorld().getPlayers().size() == 1) {
                Utils.info("World: " + event.getPlayer().getWorld().getName() + " unloaded!");
                World world = event.getPlayer().getWorld();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (world.getPlayers().isEmpty()) {
                            Bukkit.unloadWorld(world, false);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Main.getInstance(), 1000, 1000);
            }
        }

        if (BossManager.hasBoss(player.getUniqueId()))
            BossManager.removeBoss(player.getUniqueId());
        DeathListener.players.remove(player.getUniqueId());

        event.setQuitMessage(Messages.leaveMsg.replace("%PLAYER%", player.getName()));
    }
}
