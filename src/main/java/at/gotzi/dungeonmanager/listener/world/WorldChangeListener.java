// This class was created by Wireless


package at.gotzi.dungeonmanager.listener.world;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.commands.FlyCommand;
import at.gotzi.dungeonmanager.manager.BossManager;
import at.gotzi.dungeonmanager.manager.PartyManager;
import at.gotzi.dungeonmanager.manager.file.HunterFishManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.objects.party.Party;
import at.gotzi.dungeonmanager.objects.party.PartyMember;
import at.gotzi.dungeonmanager.objects.party.PartyRole;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldChangeListener implements Listener {

    private Main main;

    public WorldChangeListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = event.getFrom().getName().replace("dungeons//dungeoncache//", "");
        boolean bool = false;
        for (String dun : Utils.getDungeonList()) {
            if (worldName.contains(dun)) {
                bool = true;
                break;
            }
        }

        if(bool) {
            player.setGameMode(GameMode.SURVIVAL);
            if (BossManager.hasBoss(player.getUniqueId()))
                BossManager.removeBoss(player.getUniqueId());
            if (event.getFrom().getPlayers().size() == 0) {
                Utils.info("World: " + event.getPlayer().getWorld().getName() + " unloaded!");
                World world = event.getFrom();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (world.getPlayers().isEmpty()) {
                            Bukkit.unloadWorld(world, false);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(main, 1000, 1000);
            }
        } else {
            Party party = PartyManager.getParty(player);
            if(party != null) {
                PartyMember partyMember1 = null;
                for (PartyMember partyMember2 : party.getMembers()) {
                    if (partyMember2.getPlayer() == player) {
                        partyMember1 = partyMember2;
                    }
                }

                if (partyMember1.getPartyRole() == PartyRole.LEADER) {
                    Location location = player.getLocation();
                    for (PartyMember partyMember : party.getMembers()) {
                        Player target = partyMember.getPlayer();
                        if (target != player) {
                            PartyManager.addJump(target, location);
                            TextComponent msg = new TextComponent("Jump to party leader?");
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party jump"));
                            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§1Jump to your leader!")));
                            target.spigot().sendMessage(msg);
                        }
                    }
                }
            }

            if (PetManager.hasPet(player)) {
                if (PetManager.getPet(player.getUniqueId()) != null) {
                    PetManager.tpPet(player);
                } else {
                    PetManager.removePet(player);
                }
            }

            if (player.getAllowFlight()) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                    player.setFlySpeed(0.1f);
                } else if (!event.getPlayer().getWorld().getName().contains("resource")) {
                    if (FlyCommand.netherendList.contains(player.getUniqueId()) && !FlyCommand.overworldList.contains(player.getUniqueId()) || !FlyCommand.netherendList.contains(player.getUniqueId()) && FlyCommand.overworldList.contains(player.getUniqueId())) {
                        if (FlyCommand.netherendList.contains(player.getUniqueId())) {
                            if (!event.getPlayer().getWorld().getName().contains("nether") && !event.getPlayer().getWorld().getName().contains("end")) {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                player.sendMessage(Messages.flyDisabled);
                            }
                        } else if (FlyCommand.overworldList.contains(player.getUniqueId())) {
                            if (!event.getPlayer().getWorld().getName().contains("overworld")) {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                player.sendMessage(Messages.flyDisabled);
                            }
                        }
                    }
                } else {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage(Messages.flyDisabled);
                }
            }

            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                player.setFlySpeed(0.1f);
            }
            if (event.getFrom().getName().equals("openworld")) {
                if (HunterFishManager.getTarget().toString().equals(player.getUniqueId().toString()))
                    player.sendMessage(Messages.huntRemove);
            } else  if (player.getWorld().getName().equals("openworld")) {
                if (HunterFishManager.getTarget().toString().equals(player.getUniqueId().toString()))
                    player.sendMessage(Messages.huntAdd);
            }
        }
    }
}
