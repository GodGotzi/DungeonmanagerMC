package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.*;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import com.gamingmesh.jobs.Gui.GuiManager;
import com.gamingmesh.jobs.Jobs;
import com.magmaguy.elitemobs.entitytracker.EntityTracker;
import com.magmaguy.elitemobs.menus.SellMenu;
import com.magmaguy.elitemobs.npcs.NPCEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

public class PlayerEntityListener implements Listener {

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onHit(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        NPCEntity npcEntity = EntityTracker.getNPCEntity(event.getRightClicked());
        if(npcEntity != null) {
            event.setCancelled(true);
            if(npcEntity.npCsConfigFields.getFilename().equals(GroupManager.npc)) {
                if(!GroupManager.openGUIMain(player)) {
                    player.sendMessage(Messages.groupEnd);
                }
            } else if (npcEntity.npCsConfigFields.getFilename().equals(ShopManager.shopNpc)) {
                ShopManager.openGuiMain(player);
            } else if (npcEntity.npCsConfigFields.getFilename().equals(AlchemistManager.aliNpc)) {
                SellMenu sellMenu = new SellMenu();
                sellMenu.constructSellMenu(player);
            } else if (npcEntity.npCsConfigFields.getFilename().equals(JobManager.jobNpc)) {
                GuiManager guiManager = new GuiManager(Jobs.getInstance());
                guiManager.openJobsBrowseGUI(player);
            } else if (npcEntity.npCsConfigFields.getFilename().equals(ScrapperManager.scrapperNpc)) {
                ScrapperMenu scrapperMenu = new ScrapperMenu();
                scrapperMenu.constructScrapMenu(player);
            } else if (npcEntity.npCsConfigFields.getFilename().equals("back_teleporter.yml")) {
                if (DeathListener.players.containsKey(player.getUniqueId()))
                    TeleportManager.registerTeleport(player, DeathListener.players.get(player.getUniqueId()));
                else player.sendMessage(Messages.noBackMsg);
            }
        }

        if (event.getRightClicked().getUniqueId().toString().equals(HunterFishManager.getFish().toString()))
            event.setCancelled(true);

        ClaimManager claimManager = new ClaimManager(player);
        if (player.getLocation().getWorld().getName().equals(Locations.openWorldSpawn.getWorld().getName())) {
            if (player.getTargetEntity(5) != null) {
                UUID targetUUID = claimManager.getClaimOwnerByChunk(player.getTargetEntity(5).getChunk().getChunkKey());
                if (targetUUID != null) {
                    if (targetUUID != player.getUniqueId() && !PlayerData.getTrustedRAM(targetUUID).contains(player.getUniqueId().toString())) {
                        event.setCancelled(true);
                        player.sendMessage(Messages.restricted);
                    }
                }
            }
        }
    }

}
