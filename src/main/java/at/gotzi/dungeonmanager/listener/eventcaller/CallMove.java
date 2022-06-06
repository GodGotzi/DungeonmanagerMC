package at.gotzi.dungeonmanager.listener.eventcaller;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.events.MoveEvent;
import at.gotzi.dungeonmanager.manager.player.InteractManager;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonSetterTimer;
import at.gotzi.dungeonmanager.manager.file.BasicConfigManager;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class CallMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        String msg = Utils.color(Main.getInstance().getConfig().getString("playerMsgStarting"));
        if(Bukkit.getCurrentTick() % 2 == 0) {
            MoveEvent moveEvent = new MoveEvent(event.getPlayer(), event.getFrom(), event.getTo());
            Bukkit.getPluginManager().callEvent(moveEvent);
        }

        Player player = event.getPlayer();

        if(DungeonSetterTimer.isStarting) {
            player.teleport(from);
            if (!BasicConfigManager.noActionBar.contains(player.getUniqueId())) {
                this.sendActionbar(player, msg);
            }
            return;
        }

        if(event.getFrom().getBlock() != event.getTo().getBlock()) {
            Block block = event.getTo().getBlock();
            Block belowBlock = event.getPlayer().getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
            if (belowBlock.getType() == Material.LAVA || belowBlock.getType() == Material.WATER) {
                if (SkillCommand.isDisabled(player, true)) return;
                InteractManager interactManager = new InteractManager(player.getUniqueId(), player.getLocation());
                if (interactManager.isAllowedStructure()) {
                    List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
                    boolean bool = false;
                    if (belowBlock == null || belowBlock.getType() == Material.AIR) return;

                    if (belowBlock.getType() == Material.LAVA) {
                        for (Skill sks : skills) {
                            if (sks.getSkills() == Skills.LAVAWALK) {
                                bool = true;
                                break;
                            }
                        }

                        if (bool) {
                            if (bool) {
                                belowBlock.setType(Material.MAGMA_BLOCK);
                            }
                        }
                    } else if (belowBlock.getType() == Material.WATER) {
                        for (Skill sks : skills) {
                            if (sks.getSkills() == Skills.LAVAWALK) {
                                bool = true;
                                break;
                            }
                        }

                        if (bool) {
                            if (bool) {
                                belowBlock.setType(Material.PACKED_ICE);
                            }
                        }
                    }
                }
            }
        }

        ClaimManager claimManager = new ClaimManager(player);
        if(from.getWorld().getName().equals(Locations.openWorldSpawn.getWorld().getName())) {
            UUID uuidTo = claimManager.getClaimOwnerByChunk(to.getChunk().getChunkKey());
            UUID uuidFrom = claimManager.getClaimOwnerByChunk(from.getChunk().getChunkKey());
            if (uuidTo != null) {
                if (uuidTo != player.getUniqueId() && PlayerData.getBannedRAM(uuidTo).contains(player.getUniqueId().toString())) {
                    player.teleport(from);
                    player.sendMessage(Messages.banned);
                }
            }

            if (to.getChunk() != from.getChunk()) {
                if (uuidTo != null && uuidFrom != null) {
                    if (uuidFrom.toString().equals(player.getUniqueId().toString()) && !uuidTo.toString().equals(player.getUniqueId().toString())) {
                        this.sendActionbar(player, Messages.enterPlot.replace("%OWNER%", PlayerData.getName(uuidTo)));
                    } else if (uuidTo.toString().equals(player.getUniqueId().toString()) && !uuidFrom.toString().equals(player.getUniqueId().toString())) {
                        this.sendActionbar(player, Messages.enterHome);
                    }
                } else if (uuidTo == null && uuidFrom != null) {
                    if (uuidFrom.toString().equals(player.getUniqueId().toString())) {
                        this.sendActionbar(player, Messages.leaveHome);
                    } else {
                        this.sendActionbar(player, Messages.leavePlot.replace("%OWNER%", PlayerData.getName(uuidFrom)));
                    }
                } else if (uuidTo != null && uuidFrom == null) {
                    if (uuidTo.toString().equals(player.getUniqueId().toString())) {
                        this.sendActionbar(player, Messages.enterHome);
                    } else {
                        this.sendActionbar(player, Messages.enterPlot.replace("%OWNER%", PlayerData.getName(uuidTo)));
                    }
                }
            }
        }
    }

    private void sendActionbar(Player player, String msg) {
        if (BasicConfigManager.noActionBar.contains(player.getUniqueId())) return;
        BasicConfigManager.noActionBar.add(player.getUniqueId());
        player.sendActionBar(msg);
        new BukkitRunnable() {
            @Override
            public void run() {
                BasicConfigManager.noActionBar.remove(player.getUniqueId());
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 60);
    }
}
